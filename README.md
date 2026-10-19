# MedEx — технический прототип Compose Multiplatform

Одноразовый прототип для пресейла MedEx: общий Compose-код на Android, iOS и Web (Kotlin/Wasm + Kotlin/JS).
Цель — проверить пригодность Compose Web: производительность UI, корутины в однопоточном браузере, нативные возможности.
Выжимка исследования — [docs/research/2026-10-14-cmp-web-orbit.md](docs/research/2026-10-14-cmp-web-orbit.md).

## Запуск

| Платформа | Команда |
| --- | --- |
| Android | `./gradlew :androidApp:installDebug` |
| iOS | открыть `iosApp/iosApp.xcodeproj` в Xcode и запустить схему `iosApp` |
| Web (Wasm) | `./gradlew :webApp:wasmJsBrowserDevelopmentRun` → http://localhost:8080 |
| Web (JS) | `./gradlew :webApp:jsBrowserDevelopmentRun` |
| Web, production | `./gradlew :webApp:wasmJsBrowserDistribution` → `webApp/build/dist/wasmJs/productionExecutable` |
| Web, wasm + js fallback | `./gradlew :webApp:composeCompatibilityBrowserDistribution` |

Тесты (одинаковый набор на всех платформах):
`./gradlew :shared:testAndroidHostTest :shared:iosSimulatorArm64Test :shared:wasmJsTest :shared:jsTest`

## Архитектура

Один модуль `shared`, пакеты по фичам (`org.dimas.medextest`):

```
app/        MedExApp (гейт онбординга, адаптивность), MainScaffold, navigation/ (AppNavigator, сборка entries и deep links), di/
core/       common, coroutines, storage, mock (MockBackend), location, media, navigation, platform,
            time, format, designsystem (+ adaptive)
feature/    home, marketplace, chat, medcard, more (+ more/capabilities, more/devsettings), onboarding, checkin, profile
            структура и правила — в CLAUDE.md
```

- **MVI** — Orbit 12: `ViewModel` реализует `OrbitContainerHost` и интерфейс `XActions`; экран — stateless `XScreen(state, actions)`.
- **Данные** — мок-репозитории через `MockBackend` (задержка, ошибки, пустые ответы настраиваются в «Ещё → Для разработчика»).
- **Навигация** — Navigation 3: свой `AppNavigator` со стеком на вкладку; в вебе — своя привязка к истории браузера (`#/more/capabilities`).
- **Платформа** — геолокация, хранилище и камера в вебе без сторонних библиотек; выбор файлов — FileKit.

Шрифты, иллюстрации и фото пока заглушки (`core/designsystem/illustration`), заменяются без правок экранов.
