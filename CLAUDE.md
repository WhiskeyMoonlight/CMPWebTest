# MedExTest — правила кода

Прототип Compose Multiplatform (Android, iOS, Web), пакет `org.dimas.medextest`. Модули: `shared` — приложение (UI и логика),
`compute` — чистые вычислительные ядра без UI и корутин (SHA-256, разбор анализов, фильтр снимка, сериализуемые задания для воркера),
`webWorker` — Web Worker на Kotlin/Wasm и Kotlin/JS поверх `compute`; его бандлы `webApp` копирует в `worker/wasm` и `worker/js`.

## Код
- Никаких комментариев и KDoc. Если без комментария непонятно — переписать код (имена, разбиение), а не комментировать.
- Один файл — одна сущность: класс, интерфейс, enum, object или набор extension-функций одного назначения.
  Маленькие `private`-хелперы и composable-кусочки, нужные только этому файлу, остаются рядом.
- UI-компоненты фичи — `internal`, каждый в своём файле. Публичны только `XRoute`/`XScreen` и то, что нужно другим фичам.
- Хелперы и утилиты (форматирование, построение URL, маппинг) — в отдельных файлах `util/`, не внутри экранов.
- Не экономить на файлах: доменные модели, MVI-контракт, UI-модели — каждый в своём файле.

## Структура фичи
Фичи совпадают с destination-ами нижней панели: `home`, `marketplace`, `chat`, `medcard`, `more`
(подфичи `more/capabilities`, `more/devsettings`, `more/loadlab` — секция нагрузки внутри дев-меню), плюс `onboarding`, `checkin`, `profile`.

```
feature/<name>/
  domain/model/        одна модель — один файл
  domain/repository/   интерфейсы
  domain/usecase/      один use case — один файл
  data/                реализации репозиториев
  presentation/
    <Name>ViewModel.kt
    mvi/               <Name>State, <Name>Actions, <Name>SideEffect
    model/             UI-модели
    ui/                <Name>Route, <Name>Screen, компоненты (internal)
    util/              хелперы экрана
  navigation/          <Name>NavKey, <name>Entries (EntryProviderScope extension), <name>DeepLinks
  di/<Name>Module.kt   Koin-модуль фичи
```

## Архитектура
- MVI — Orbit 12. ViewModel реализует `OrbitContainerHost` и интерфейс `<Name>Actions`; экран — stateless `<Name>Screen(state, actions)`.
  Настройки Orbit задаются один раз (`Orbit.configureDefaults`), в ViewModel только `orbitContainer(State())`.
- DI — Koin, модули по фичам и сущностям (`storage`, `location`, `platform`, `mock`, `time`, `coroutines`).
  Платформенные модули — `expect val xxxModule: Module`.
- Диспетчеры инжектятся напрямую (`CoroutineDispatcher` с `named(DispatcherQualifier.X)`), без обёрток. IO — `expect/actual` там, где нужен.
- Навигация — Navigation 3. Фича объявляет свои `NavKey`, `fun EntryProviderScope<NavKey>.xEntries(...)` и `xDeepLinks`;
  `app` только собирает. Фичи навигируют через `core.navigation.Navigator`, переходы в другие фичи — колбэками.
- Тяжёлая работа — через `core.compute.ComputeExecutor` с режимами `ExecutionMode` (Main, Default, Parallel, Cooperative — кванты 6 мс
  с уступкой кадра через `UiYielder`, Worker — пул `WebWorkerBackend`, только веб). Долгие задачи — `core.background.BackgroundTasks`
  в scope приложения (`named(ScopeQualifier.Application)`): переживают уход с экрана, отменяются по id, отдают прогресс раз в 100 мс.
- Производительность: `core.perf.PerfMonitor` (кадры через `withFrameNanos`, лаг UI-потока), HUD включается в дев-меню.
- Адаптивная вёрстка: `WindowWidthClass` (Compact/Medium/Expanded). Фоны во всю ширину, контент ограничен по ширине,
  на широких экранах — NavigationRail и многоколоночная раскладка.
- Сторонние библиотеки — только официальные JetBrains/Google или согласованные (Orbit, Koin, FileKit). Остальное — свои адаптеры.

## Проверка
- Тесты в `commonTest`, имена в snake_case:
  `./gradlew :shared:testAndroidHostTest :shared:iosSimulatorArm64Test :shared:wasmJsTest :shared:jsTest :compute:allTests`
- Браузерные тесты (`webTest`): адаптеры геолокации/файлов/камеры со стабами браузерных API, характеризация event loop и UI-тесты
  Compose (`androidx.compose.ui.test.v2.runComposeUiTest`, в т.ч. сквозной «нагрузка + навигация»). Ждать фоновую работу только через
  `awaitCondition` из `webTest/testutil` — `waitUntil` на вебе синхронный и не отдаёт управление event loop.
  Для Kotlin/JS в Karma Skia поднимает `shared/karma-bootstrap/skiko-globals.js` (+ `karma.config.d`, `webpack.config.d`).
  Эти конфиги общие для js и wasm — всё js-специфичное держать под `if (__dirname.includes('/build/js/'))`. Gradle не видит
  их правок как входы задачи: после изменения запускать тесты с `--rerun`.
- Замеры в браузере: production-дистрибутив на порту 8081 (конфиг превью `web-wasm-prod`) и `node tools/perf/run-load-matrix.mjs`,
  нативные функции — `tools/perf/native-check.mjs` (см. `tools/perf/README.md`, мост `?perf-bridge=1`).
- Сборка: `./gradlew :androidApp:assembleDebug :webApp:wasmJsBrowserDistribution :shared:compileKotlinIosSimulatorArm64`
- Публикация веба: `.github/workflows/pages.yml` при push в `main` собирает `composeCompatibilityBrowserDistribution`
  (wasm + откат на Kotlin/JS), удаляет `*.map` и выкладывает в GitHub Pages (Settings → Pages → Source: GitHub Actions).
  Сайт публичный, в `index.html` — `noindex, nofollow`; `robots.txt` в проекте не работает (читается только из корня домена).
  Целиком `./gradlew build` не зелёный по известным причинам: `shared:wasmJsBrowserTest` (баг раннера, Gradle 9 считает «0 тестов» ошибкой),
  `linkDebugTestIosSimulatorArm64` (Xcode 16.2), иногда `linkReleaseFrameworkIosSimulatorArm64` — OOM Kotlin/Native при параллельной сборке.
  Не просить в одном запуске development- и production-webpack одного модуля: они пишут в общий `build/*/packages/<модуль>`, Gradle 9 падает.
- `kotlin.js.ExperimentalWasmJsInterop` включён в Gradle для web/js/wasmJs source set'ов (`languageSettings.optIn`) — `@OptIn` в файлах не нужен.
  После правок `build.gradle.kts` — Sync в IDE, иначе Android Studio продолжит показывать предупреждения.
- В `webMain`/`webTest` явно импортировать `kotlin.js.*` (`JsAny`, `JsString`, `js`, `toJsString`, `toJsNumber`): Gradle подставляет их сам,
  а Android Studio — нет. Без импорта у `external interface X : JsAny` супертип в IDE не разрешается, и в соседних модулях
  IDE пишет «X не JsAny» (`postMessage`, `unsafeCast`), хотя Gradle всё собирает.
- Веб локально: `./gradlew :webApp:wasmJsBrowserDevelopmentRun` (конфиг превью `web-wasm` в `.claude/launch.json`, порт 8080).

## Контекст проекта
- Одноразовый технический прототип для пресейла MedEx (клиника, дочерняя компания Сбера). Цель — вердикт по Compose Web
  (Kotlin/Wasm): производительность UI (включая 120 Гц), корутины в однопоточном браузере, нативные возможности в браузерах.
- Рамка — ТЗ на техдемо от 2026-10-13: `/Users/dmitriymalyshev/AndroidStudioProjects/presale-repo/clients/MedEx/Платформа проактивного управления здоровьем пациента/ТЗ на техническое демо Compose Multiplatform — 2026-10-13.md`
  (Главная + Предвизитная анкета UC_050, FileKit, PWA, push → deep link, замеры 120 Гц, выкладка по HTTPS).
  Сверх ТЗ пользователь хочет «боевой» слой данных (репозитории, долгие операции) и замеры корутин на вебе.
- Исследование и все находки: `docs/research/2026-10-14-cmp-web-orbit.md`.

## Стек и принятые решения (2026-10-14 — 2026-10-15)
- Kotlin 2.4.21, CMP 1.12.1, AGP 9.1.1, Gradle 9.5.1, lifecycle 2.11.0, Navigation 3 (`navigation3-ui` 1.2.0-rc01,
  `lifecycle-viewmodel-navigation3` 2.11.0), Orbit 12.0.1, Koin 4.2.2 (DSL из `org.koin.core.module.dsl`), FileKit 0.16.0,
  kotlinx-coroutines 1.11.0, kotlinx-datetime 0.8.0, kotlin-wrappers 2026.9.3 (`webMain`), kotlinx-serialization-json 1.11.0 (`compute`), compose `ui-test` (`webTest`). minSdk 26. Kotlin daemon — 6 ГБ.
- Отклонено: Compass, multiplatform-settings, terrakok/navigation3-browser, Navigation 2 (`bindToBrowserNavigation` теряет
  состояние вкладок), sealed Action + `when`, обёртка `AppDispatchers`, `orbitSettings(tag)`, общий `RouteCodec`, «телефонная» колонка 640 dp на вебе.
- Навигация: `AppNavigator` — стек на каждую `TopLevelDestination`; `rememberDecoratedNavEntries` на вкладку, чтобы ViewModel
  и состояние вкладок жили при переключении; онбординг вне навигации (гейт в `MedExApp`). Веб: `BrowserHistoryBinding`/`BrowserHistorySync` —
  hash `#/<path>`, в `history.state` индекс записи, возврат на предыдущую запись идёт через `history.back()`.
- Deep links: `core.navigation.DeepLink` / `deepLink<K>()` / `DeepLinks` поверх Nav3 `UriDeepLinkMatcher`. Пути: `home`, `marketplace`,
  `marketplace/section/{sectionSlug}`, `chat`, `medcard`, `more`, `more/capabilities`, `more/dev`.
- Текстовые поля: `blockingIntent` в Orbit на js/wasm нет → `TextFieldState` хранится во ViewModel вне state, производное — через `snapshotFlow`.
- Действия, требующие жеста пользователя (выбор файла, камера, `tel:`, буфер обмена), вызываются прямо из `onClick` в UI — иначе браузер их блокирует.
- Иконки и логотип — `ImageVector` в коде, а не compose resources: на вебе ресурсы грузятся асинхронно и всплывают после первого кадра.
- Чек-ин (BottomSheet) — раз в календарный день (`medex.checkin.lastPrompted` / `lastSubmitted`), проверяется на каждой подписке Главной.
- Мок-бэкенд (`MockBackend`) управляется из «Ещё → Для разработчика»: задержка, доля ошибок, пустые ответы, сброс онбординга и чек-ина.
- Платформенные адаптеры без библиотек: геолокация (`LocationManagerCompat` / `CLLocationManager` / `navigator.geolocation`
  с отменяемой обёрткой), хранилище (SharedPreferences / NSUserDefaults / localStorage), камера на вебе — скрытый `<input capture>`.
- Ассеты — заглушки (системные шрифты, иллюстрации на Canvas), ждём экспорты из Figma. Figma MCP: на месте пользователя лимит 20 вызовов в месяц.

## Состояние на 2026-10-15
- Готово: онбординг, Главная + чек-ин, Витрина (секции грузятся независимо, поиск с debounce), «Ещё» + «Возможности устройства»
  + «Для разработчика», заглушки Медкарты и AI-чата. Адаптивная вёрстка Compact / Medium / Expanded (NavigationRail, две колонки).
- Проверено вручную: Android-эмулятор, iOS-симулятор (Xcode 16.2; приложение ставилось через `xcrun simctl`, доступ к панели симулятора
  не выдан), Chrome через DevTools MCP (deep links, `popstate`, `history.back`, ширина 1440 px).
- Нагрузка (2026-10-15): дев-меню → «Нагрузка: корутины и потоки» — 4 сценария (импорт анализов, SHA-256 вложений, фильтр снимка,
  шторм запросов) и SHA-256 выбранного файла, режимы и объём ×1/×4/×10, отмена, отчёт в буфер обмена, HUD поверх всех экранов.
  Замеры в Chrome (wasm и js), с замедлением CPU ×4 и на Android-эмуляторе — в исследовании, раздел «Нагрузка, корутины и многопоточность».
- Тесты: JVM 112/112 (`shared`) + 27/27 (`compute`), Kotlin/JS в headless Chrome 154/154 (`shared`) + 27/27 (`compute`).

## Известные проблемы
- iOS: тестовый бинарник не линкуется на Xcode 16.2 — `ld: framework '_LocationEssentials' not found` (платформенные библиотеки Kotlin 2.4.2x
  собраны против iOS 26 SDK). Приложение собирается. Нужен Xcode 26.x (на macOS 15.6 максимум 26.3).
- Wasm: тест-раннер падает на старте — `ClassCastException: Cannot cast instance of Object to Node` в `web.dom.__JsClosureToKotlinClosure_((Js)->Js)`
  из `kotlin.test.adapter` (Kotlin 2.4.20 и 2.4.21). Похоже на баг компилятора Kotlin/Wasm; нужен минимальный репро и issue в YouTrack.
- Тесты deep links лежат в `webTest`: на Android `DeepLinkUri` — это `android.net.Uri`, в host-тестах без Robolectric он не работает.
- Веб: в скрытой вкладке lifecycle уходит в STOP и `requestAnimationFrame` не вызывается (подписки Orbit и отрисовка стоят);
  эмодзи — «тофу» до догрузки Noto с fonts.gstatic.com; `FontFamily.Serif` недоступен. Нужны свои шрифты и эмодзи-картинки.
- `SnapshotStateList.equals(List)` — `true` на JVM и `false` на iOS/wasm/js; в тестах сравнивать через `toList()`.
- Хук GateGuard в основной сессии отклоняет первую запись каждого нового файла (субагентов не трогал); новые файлы удобнее создавать через heredoc в Bash.
- Веб: CPU-работа в Main/Default/Parallel замораживает UI на всё время задачи; атомарные операции (разбор страницы JSON, декодирование
  картинки, чтение файла в `ByteArray`) не режутся на кванты. `Int8Array → ByteArray` в Kotlin/Wasm поэлементный и медленный.
- Веб: `tel:` и карта открываются через `window.open(uri, "_blank")` — `tel:` на десктопе даёт пустую вкладку.
- Верхняя панель дев-меню прокручивается вместе с контентом; на Android прокручиваемый контент заезжает под статус-бар.
- Панель браузера Claude, когда скрыта, троттлит `requestAnimationFrame` — кадры мерить в своём headless Chrome (`tools/perf`).

## Следующие шаги
- Экспорты шрифтов (с кириллицей) и иллюстраций из Figma, эмодзи как картинки.
- HTTPS-стенд от DevOps (`<домен KTS>/medex/`) — без него на телефонах не проверить камеру, геолокацию, PWA и Web Push.
- Нагрузка: передавать в воркер данные порциями без полного `ByteArray`, `tel:` через навигацию текущей страницы, повторить матрицу
  замеров на реальных телефонах (Android Chrome, iOS Safari) после HTTPS-стенда; замеры 120 Гц.
- По ТЗ демо: Предвизитная анкета (UC_050) с вложениями, PWA (manifest + Service Worker), push → deep link на анкету, замеры 120 Гц.
- Поставить Xcode 26.3 и проверить iOS-тесты; сделать минимальный репро wasm-бага тест-раннера.
