# Исследование: CMP Web, корутины на web, нативные API, Orbit MVI

Дата: 2026-10-14. Источники — официальные доки kotlinlang, исходники CMP 1.12.1 / kotlinx.coroutines / Orbit 12.0.1, Maven Central.
Пометка «не проверено» — вывод из исходников или спецификаций, не подтверждён запуском.

Рамка работ: [ТЗ на техническое демо CMP](../../../presale-repo/clients/MedEx/Платформа%20проактивного%20управления%20здоровьем%20пациента/ТЗ%20на%20техническое%20демо%20Compose%20Multiplatform%20—%202026-10-13.md)
(Главная + Предвизитная анкета, FileKit, PWA, push → deep link, замеры 120 Гц).

## Версии (на 14.10.2026)

| Компонент | В проекте | Актуальная | Комментарий |
| --- | --- | --- | --- |
| Kotlin | 2.4.20 | 2.4.21 (08.10) | bugfix; ТЗ демо ссылается на 2.4.21 |
| Compose Multiplatform | 1.12.1 | 1.12.1 / 1.13.0-alpha02 | CMP Web = Beta с 1.9.0 |
| AGP / Gradle | 9.1.1 / 9.5.1 | — | в пределах таблицы совместимости Kotlin 2.4.2x |
| lifecycle | 2.11.0 | 2.11.0 | js + wasmJs поддерживаются |
| navigation-compose | — | 2.10.0-beta01 (в составе CMP 1.12.1), 2.10.0-rc01 | `NavController.bindToBrowserNavigation()` |
| kotlinx-coroutines | транзитивно | 1.11.0 | |
| kotlinx-serialization | — | 1.11.0 | |
| Ktor | — | 3.6.0 | `ktor-client-js` (fetch) публикует и wasm-вариант |
| Koin | — | 4.2.2 | js + wasmJs, `koin-compose-viewmodel` |
| Orbit MVI | — | 12.0.1 (28.08.2026) | js + wasmJs с 10.0.0 |
| FileKit | — | 0.16.0 | web: только file/dir picker, камеры и saver нет |
| Compass (геолокация) | — | 4.0.1 | `geolocation-browser` для js + wasmJs |
| kotlin-wrappers | 2026.9.3 | 2026.10.0 (собран Kotlin 2.4.21) | держать 2026.9.3, пока Kotlin 2.4.20 |

Xcode: Kotlin 2.4.2x протестирован с Xcode 26.4. Локально стоит 16.2 — жёсткого блока нет, но связка не тестировалась.
macOS 15.6 позволяет поставить максимум Xcode 26.3. App Store с 28.04.2026 принимает только сборки Xcode 26+.
`iosX64` удалён из CMP с 1.11 — только `iosArm64` + `iosSimulatorArm64` (у нас уже так).

## Web: wasmJs и js

- Compose for Web = Kotlin/Wasm (`wasmJs`, нужен WasmGC) + Skia (Skiko, `skiko.wasm`), рисующая в `<canvas>` через WebGL2.
- `js` target — fallback для браузеров без WasmGC. Он **тоже грузит `skiko.wasm`**, то есть «совсем без Wasm» не работает.
- Раскатывать отдельно не нужно: `./gradlew :webApp:composeCompatibilityBrowserDistribution` собирает оба таргета в один дистрибутив
  с загрузчиком, который через `WebAssembly.validate()` выбирает wasm или js. Вывод: `webApp/build/dist/composeWebCompatibility/productionExecutable`.
- Общий код обоих веб-таргетов — source set `webMain` (default hierarchy, Kotlin 2.2.20+).
- Задачи: `wasmJsBrowserDevelopmentRun`, `jsBrowserDevelopmentRun`, `wasmJsBrowserDistribution`, `jsBrowserDistribution`.
- Поддержка WasmGC: Chrome/Edge/Chrome Android 119+, Firefox 120+, Safari macOS/iOS 18.2+. Все браузеры на iOS = WebKit.
- iOS Lockdown Mode отключает WebAssembly целиком — не работает ни wasm, ни js-fallback. Нужна заглушка в `index.html`.

### Вес и рендер

- Замер KotlinConf (CMP 1.12.1, gzip): `skiko.wasm` 3.36 МБ + app `.wasm` 1.96 МБ + JS glue 0.15 МБ ≈ 5.5 МБ до первого кадра.
- Системных шрифтов у canvas нет. CMP 1.12 подгружает недостающие глифы с `fonts.gstatic.com`; настройка `fontFallbackUrl` появится
  только после 1.12.1 → кладём свой шрифт с кириллицей в `composeResources` и прогреваем `preloadFont()`.
- A11y: скрытое DOM-дерево `cmp_a11y_root`. IME — через скрытый `<input>`. Поиск по странице и браузерное выделение на canvas не работают (не проверено).
- `HtmlElementView` (бывший `WebElementView`) — HTML-элемент поверх canvas; события в его области Compose не видит.
- Размытие при pinch-zoom исправлено только в 1.13.0-alpha01. Safe-area в 1.12.1 — через CSS `env()` и `viewport-fit=cover`.

## Корутины и потоки на web

- Kotlin/Wasm в браузере однопоточный. Официальной многопоточности нет, proposal shared-everything-threads на ранней стадии.
- `Dispatchers.Default` = `WindowDispatcher`: очередь microtask'ов, каждые 16 задач отдаёт управление event loop через `postMessage`.
- `Dispatchers.Main` делегирует в Default — **один и тот же поток**. Recomposer CMP Web живёт на Main, кадр — по `requestAnimationFrame`.
- `Dispatchers.IO` и `runBlocking` на js/wasmJs **нет**. Код с ними в `commonMain` не скомпилируется → инжектим диспетчеры.
- Сетевой I/O (fetch) не блокирует: браузер делает его вне JS-потока, параллельные запросы реально параллельны.
- CPU-работа (парсинг большого JSON, сортировка/фильтрация, крипто, обработка изображений) замораживает canvas и ввод.
  Митигации:
  - нарезка с бюджетом времени (~8 мс), затем `delay(1)` (macrotask). `yield()` даёт отрисовку только раз в ~16 продолжений, `delay(0)` не приостанавливает;
  - Web Worker: отдельный модуль, обмен сообщениями, общих Kotlin-объектов нет. Официального Gradle-шаблона нет.

## Нативные API в браузере

- Вызываем Web API, браузер показывает системный UI ОС: `<input type=file>` → iOS sheet «Снять фото / Медиатека / Файлы»,
  Android chooser; geolocation → системный permission prompt.
- **Secure context обязателен** для geolocation, камеры, Service Worker, Web Push. `http://192.168.x.x` — не secure context.
  Для тестов на телефоне: стенд с HTTPS, HTTPS-туннель или `chrome://inspect` port forwarding (на устройстве будет `localhost`).
- **User activation**: file picker, clipboard, `window.open`, `navigator.share`, запрос разрешения на уведомления (iOS) требуют жеста.
  CMP Web вызывает `onClick` синхронно внутри DOM-обработчика pointer-события — activation засчитывается (не проверено, вывод по исходникам).
  Окно ~5 с. Нельзя делать `await` сети перед `picker.launch()`. Запасной вариант для Safari — настоящий `<input>` через `HtmlElementView`.
- Web Push на iPhone — только в установленной PWA, iOS 16.4+, разрешение — по жесту.
- Библиотеки с web: FileKit 0.16.0 (file picker), Compass 4.0.1 (геолокация), Calf 0.14.0 (file picker).
  Без web: moko-geo, moko-permissions, Peekaboo. KMP-библиотеки разрешений с поддержкой web нет: браузер спрашивает сам,
  статус — `navigator.permissions.query`.
- Типизированные DOM API: kotlin-wrappers `kotlin-browser` (пакеты `web.*`, js + wasmJs, suspend-обёртки над geolocation и getUserMedia).

## Навигация, хранилища

- Navigation Compose: `bindToBrowserNavigation()` (hash-маршруты `#...`, Back/Forward браузера). Navigation 3 на web без привязки к истории (CMP-8924).
- Web lifecycle: `visibilitychange` → START/STOP, `focus`/`blur` → RESUME/PAUSE; CREATED и DESTROYED не достигаются.
- Хранилища на web: multiplatform-settings 1.3.0 (localStorage), DataStore 1.3.0-rc01 (OPFS), Room 3 и SQLDelight 2.4.1 через Web Worker.

## Orbit MVI 12.0.1

- Артефакты для wasmJs/js: `orbit-core`, `orbit-viewmodel` (KMP `ViewModel`), `orbit-compose`, `orbit-test`.
  Собраны на Kotlin 2.1.21, lifecycle 2.9.0, CMP 1.8.1 — Gradle поднимет зависимости, сборку на нашем стеке надо проверить.
- 12.0: `ContainerHost<S, SE>` → `OrbitContainerHost<INTERNAL, EXTERNAL, SE>`, `container()` → `orbitContainer()`; старые имена deprecated.
  `test()` deprecated → `testWithInternalState(...)`. Диспетчеры в настройках задаются фабриками `() -> CoroutineDispatcher`.
- Явно указывать тип `container` (issue #236 — рекурсивный вывод типов).
- Потоки: event loop на `Dispatchers.Default`, интенты стартуют на `Unconfined`. **Интенты конкурентны**, не сериализуются.
  `reduce` = CAS (`MutableStateFlow.update`), лямбда может выполниться повторно → должна быть чистой.
  Read-modify-write через `state` вне `reduce` даёт потерянные обновления. Отмена предыдущей загрузки — вручную через `Job`.
- **`blockingIntent` на js/wasmJs отсутствует** (объявлен в `jvmAndNativeMain`, реализован через `runBlocking`).
  Для текстовых полей официально — `TextFieldState` + `snapshotFlow`, либо держать текст в UI-состоянии.
- Исключения по умолчанию не перехватываются → задать `exceptionHandler`.
- Side effects (`FAN_OUT`): буферизуются до появления подписчика, один потребитель, при полном буфере `postSideEffect` приостанавливается.
- Логирования интентов и time-travel нет (MVIKotlin 4.4.0 их имеет; wasmJs там поддержан с 4.0.0).
- Возможная регрессия 12.x: состояние не пишется в `SavedStateHandle`, если UI подписан только через `collectAsState()` (не проверено).
- Тесты `orbit-compose` в браузере у самих авторов отключены (CMP-8235).
- Как отдавать действия: официально — публичные функции на хосте, колбэки передаются в экран «напрямую или через интерфейс».
  Официальной позиции про `sealed Action` + `when` нет, это паттерн сообщества (даёт одну точку логирования).
- Референсы: `orbit-mvi/samples/orbit-posts-compose-multiplatform`, `appmattus/crypto/samples/composeApp`
  (сопровождающий Orbit, wasmJs, `TextFieldState` вместо `blockingIntent`).

## Как мерить

- Кросс-платформенный счётчик кадров на `withFrameNanos` (p50/p95/p99, доля > 8.33/16.7 мс).
- Web: Chrome DevTools Performance (CPU throttling), `performance.mark/measure` через interop (видны в треке Timings),
  Long Animation Frames API (только Chromium — проверять feature detection). Android Chrome — `chrome://inspect`, iOS Safari — Web Inspector.
- Бенчмарки JetBrains: `compose-multiplatform/benchmarks/multiplatform` (LazyGrid, AnimatedVisibility, режимы VSYNC_EMULATION/STARTUP).
- Throughput корутин в браузере — свой микробенч на `TimeSource.Monotonic` (kotlinx-benchmark гоняет wasm в Node/D8, не в браузере).

## Наблюдения при реализации (2026-10-14)

- Стек собрался и прошёл 33 общих теста на JVM, iOS-симуляторе, wasmJs и js (Chrome headless). Orbit 12.0.1, Koin 4.2.2,
  Nav3 1.2.0-rc01, FileKit 0.16.0 работают на Kotlin 2.4.20 / CMP 1.12.1. iOS-симулятор собирается на Xcode 16.2.
- `SnapshotStateList.equals(List)` — `true` на JVM и `false` на iOS/wasm/js: одинаковый тест падал только на не-JVM таргетах.
- Скрытая вкладка: CMP Web переводит lifecycle в STOP (`visibilitychange`), Orbit `collectAsState`/`repeatOnSubscription`
  останавливаются, а `requestAnimationFrame` не вызывается — Compose не рисует новые кадры, пока вкладка не видна.
- Эмодзи (👋, 🔒) при первом кадре — «тофу», затем догружается Noto Color Emoji с `fonts.gstatic.com`. `FontFamily.Serif`
  в вебе недоступен — заголовки рисуются без засечек (на Android/iOS засечки есть). Нужны свои шрифты и эмодзи-картинки.
- Линковка wasm dev-сборки упала с OOM при `kotlin.daemon.jvmargs=-Xmx3072M`, когда параллельно шли тесты.
- Nav3: состояние вкладок сохраняется через `rememberDecoratedNavEntries` на каждую вкладку; NavDisplay 1.2 сам даёт
  записям lifecycle, поэтому side effects Orbit внутри Nav3-экранов доходят (issue #319 не воспроизвёлся).
- Своя привязка к истории браузера: deep link `#/more/capabilities`, `popstate` и `history.back()` проверены в Chrome.
- Kotlin/Native: в companion object наследника ObjC-класса нельзя хранить поля (делегат `CLLocationManager`).

## Наблюдения после рефакторинга (2026-10-15)

- Nav3 1.2 даёт официальный разбор deep link (`UriDeepLinkMatcher` + `withBackStack`, ключи через kotlinx.serialization),
  но не обратную сборку URL. На Android `DeepLinkUri` — это `android.net.Uri`: в host-тестах без Robolectric не работает,
  поэтому тесты deep links лежат в `webTest`.
- `org.koin.compose.viewmodel.dsl.viewModelOf` в Koin 4.2.2 deprecated — используем `org.koin.core.module.dsl`.
- iOS: тестовый бинарник Kotlin/Native не линкуется на Xcode 16.2 — `ld: framework '_LocationEssentials' not found`.
  Платформенные библиотеки Kotlin 2.4.2x собраны против iOS 26 SDK, где CoreLocation зависит от `_LocationEssentials`.
  Само приложение (статический фреймворк, финальная линковка в Xcode) собирается и работает. Лечится Xcode 26.x.
- Wasm: тест-раннер падает на старте (`ClassCastException: Cannot cast instance of Object to Node` в
  `web.dom.__JsClosureToKotlinClosure_((Js)->Js)` из `kotlin.test.adapter`) — на Kotlin 2.4.20 и 2.4.21.
  Похоже на баг дедупликации адаптеров JS-замыканий в компиляторе Kotlin/Wasm при наличии kotlin-wrappers.
  Те же тесты проходят на JVM и Kotlin/JS; приложение в браузере работает.
- Линковке wasm-тестов не хватало 3 ГБ у Kotlin daemon — поднято до 6 ГБ.

## Нагрузка, корутины и многопоточность (2026-10-15)

Что сделано: модуль `:compute` (чистые ядра без UI: SHA-256, разбор страниц анализов на kotlinx-serialization, фильтр
снимка полосами, сериализуемые задания), модуль `:webWorker` (воркер на Kotlin/Wasm и Kotlin/JS: 250 КБ wasm + 8 КБ JS),
`ComputeExecutor` с режимами Main / Default / Parallel / Нарезка (кванты 6 мс + уступка кадра) / Web Worker
(пул `hardwareConcurrency − 1`, до 8), `BackgroundTasks` в scope приложения, HUD (`withFrameNanos` + зонд лага UI-потока),
секция «Нагрузка» в «Для разработчика», стенд `tools/perf/` (CDP, headless Chrome, `?perf-bridge=1`).

### Замеры

Chrome 155 headless, MacBook Air M4 (10 ядер), production-сборка Kotlin/Wasm, объём ×1, во время задачи каждые 300 мс
переключаются экраны и крутится колесо. Ячейка — «время, мс / худший кадр, мс».

| Сценарий | Main | Default | Parallel | Нарезка | Web Worker |
| --- | --- | --- | --- | --- | --- |
| SHA-256, 8 файлов × 8 МБ | 355 / 217 | 344 / 333 | 334 / 317 | 336 / 17 | 103 / 17 |
| Фильтр снимка 6000×4000 | 396 / 383 | 385 / 367 | 377 / 183 | 425 / 17 | 91 / 17 |
| Импорт 60 000 анализов (40 страниц, сеть 120 мс) | 922 / 50 | 1136 / 183 | 911 / 17 | 911 / 17 | 861 / 17 |
| 10 000 запросов, семафор 64, сеть 20 мс | 3392 / 17 | 3506 / 117 | 3380 / 17 | 3374 / 17 | 3380 / 17 |

Результаты (хэши, контрольные суммы, статистика) совпадают во всех режимах и платформах — это проверяют и тесты.

- Замедление CPU ×4 (DevTools): SHA-256 Default 1341 / 1333, нарезка 1673 / 33, воркер 96 / 17; фильтр 1509 / 1500,
  2076 / 33, 81 / 17; импорт в нарезке 973 / 100. Воркеры DevTools не замедляет — их выигрыш здесь завышен.
- Kotlin/JS против Kotlin/Wasm (те же сценарии, Default): SHA-256 364 и 344 мс; фильтр 113 и 385 мс (воркер 63 и 91);
  разбор JSON на Kotlin/JS медленнее — в нарезке кадр до 200 мс против 17.
- Android-эмулятор (Android 16, 4 ядра): SHA-256 Default 1010 / 16, Parallel 473 / 16, нарезка 1090 / 16, Main 1050 / 133
  (рывок на каждом файле); фильтр Default 301 / 16, Parallel 164 / 16, нарезка 372 / 16, Main 254 — ни одного кадра за задачу.
- Тесты event loop (Kotlin/JS в headless Chrome, `EventLoopResponsivenessTest`): 333 мс блокирующей работы → лаг таймера 334,
  кадр 316; нарезка + MessageChannel → 8 и 16; нарезка + `yield()` → 186 и 100. 200 уступок: `yield()` 0 мс,
  `delay(1)` 967 мс, MessageChannel 3 мс.
- Файл 64 МБ через системный выбор (Wasm): Default 580 мс, чтение 310, кадр 533; нарезка 585, кадр 250 (чтение не режется);
  воркер 1492, кадр 17 (буфер передаётся без копирования, но `Int8Array → ByteArray` внутри воркера медленный); Main 602.

### Подводные камни

1. На вебе Main, Default и Parallel — один поток. CPU-работа останавливает кадры и ввод на всё время задачи, Parallel не
   ускоряет. На Android те же Default и Parallel UI не трогают, Parallel даёт ×2 на 4 ядрах. Общий код с
   `withContext(Dispatchers.Default)` ведёт себя по-разному: для веба нужен свой путь (нарезка или воркер).
2. Кадр дают только уступки макрозадачей (MessageChannel, в Chromium ещё `scheduler.yield()`). `yield()` отдаёт кадр раз
   в ~16 продолжений, `delay(1)` стоит ~5 мс простоя из-за клэмпа вложенных таймеров. Цена нарезки — 0–10% на быстром CPU,
   25–40% при замедлении ×4, кадр 33 мс.
3. Атомарные операции не режутся: разбор страницы JSON, декодирование картинки (`decodeToImageBitmap`, 66 мс на PNG 3,4 МБ),
   чтение файла в `ByteArray` (FileKit, `Blob.byteArray`). Размер порции подбирать под слабое устройство или уносить в воркер.
4. Web Worker на Kotlin/Wasm работает: отдельный Gradle-модуль, webpack-бандл как classic worker, протокол — JSON-строки
   и `ArrayBuffer` с передачей владения. Пул из 8 даёт ×3,4–4,2 на CPU. Ограничения: общей памяти нет, данные копируются
   и сериализуются, поэлементное копирование `Int8Array → ByteArray` съедает выигрыш на больших файлах; отмена — только
   `terminate()` и новый воркер (первый прогон с холодным пулом — 104 мс против 92 мс у прогретого); `SharedArrayBuffer` потребует заголовков
   COOP/COEP на стенде.
5. Kotlin/Wasm быстрее Kotlin/JS не везде: на циклах по `IntArray` (фильтр) Wasm в 3,4 раза медленнее, SHA-256 одинаково,
   JSON быстрее на Wasm. Решение о таргете — по замерам конкретной нагрузки.
6. Тысячи корутин на вебе дешёвы: 10 000 запросов с семафором 64 — 3,4 с при теоретических 3,1, кадры 17 мс.
7. Клик во время фриза не теряется: событие обрабатывается после разблокировки, `navigator.userActivation.isActive` — true,
   выбор файла открывается, но с задержкой на всю длину фриза.
8. Измерения: на вебе фриз виден только после окончания задачи, поэтому окно замера закрывается через 120 мс после неё;
   скрытая вкладка или панель троттлит `requestAnimationFrame` до ~1 Гц; `Runtime.evaluate` по CDP отвечает и во время
   длинной JS-задачи (прерывания V8) — блокировку им не измерить, только таймерами и rAF внутри страницы.
9. UI-тесты Compose на вебе: `waitUntil` — синхронный busy-wait, корутины на реальных диспетчерах в это время не идут;
   ждать через `withContext(Dispatchers.Default) { delay() }` и `awaitIdle()`. В Karma для Kotlin/JS функции Skia не
   выставлены глобально (в приложении это делает `js-reexport-symbols.mjs`): свой bootstrap
   `shared/karma-bootstrap/skiko-globals.js` и `shared/webpack.config.d/skiko-external.js`. Таймаут Mocha по умолчанию — 2 с.

### Нативные функции в Chrome (через CDP)

- Выбор фото и файла через FileKit — работает; PNG 3,4 МБ: чтение 29 мс, декодирование 66 мс в главном потоке.
- Камера через `<input capture>` — работает. Исправлено: `input.capture = …` через IDL-свойство не ставил атрибут, теперь `setAttribute`.
- Геолокация: разрешено — координаты, запрещено — сообщение. Исправлено: после отказа приложение не перепрашивало статус
  до перезагрузки страницы, теперь статус берётся из Permissions API.
- Буфер обмена — работает. Карта и `tel:` открываются через `window.open(uri, "_blank")`: карта — новая вкладка,
  `tel:` на десктопе — пустая вкладка. Для `tel:` на вебе нужна навигация текущей страницы (не исправлено).
- UX: верхняя панель дев-меню прокручивается вместе с контентом; на Android при прокрутке контент заезжает под статус-бар.

## Производительность UI на вебе (2026-10-20)

Аудит, причины лагов на телефонах, сделанные оптимизации и замеры до/после — в
[2026-10-20-web-ui-performance.md](2026-10-20-web-ui-performance.md).

## Открытые риски для проверки руками

1. iOS-сборка Kotlin 2.4.20 на Xcode 16.2.
2. Orbit 12.0.1 на Kotlin 2.4.20 / CMP 1.12.1 / lifecycle 2.11.0 для всех таргетов.
3. User activation при открытии file picker из Compose-клика в iOS Safari.
4. Автозаполнение SMS-кода (`autocomplete="one-time-code"`) и менеджеры паролей на canvas-полях.
5. Поведение установленной PWA на iPhone: 60 Гц или выше, Web Push.
