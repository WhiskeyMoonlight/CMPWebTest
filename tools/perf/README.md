# Замеры нагрузки в браузере

Скрипты управляют собственным Chrome через DevTools Protocol (Node 22+, без зависимостей).
Приложение открывается с `?perf-bridge=1`: тогда в `globalThis.medexPerfBridge` появляются `run('Сценарий:Режим:Масштаб')`,
`tasks()` (JSON с итогами и кадрами) и `clear()`.

Перед запуском соберите production-дистрибутив и раздайте его статикой (конфиг превью `web-wasm-prod`, порт 8081):

```bash
./gradlew :webApp:wasmJsBrowserDistribution
```

```bash
python3 -m http.server 8081 --directory webApp/build/dist/wasmJs/productionExecutable
```

## Матрица «сценарий × режим»

Во время каждой задачи скрипт каждые 300 мс переключает экраны через hash и крутит колесо мыши, а в странице считает
промежутки между `requestAnimationFrame` и Long Animation Frames.

```bash
node tools/perf/run-load-matrix.mjs
```

Переменные: `BASE_URL`, `SCENARIOS` (`LabImport,AttachmentHash,ScanFilter,RequestStorm`),
`MODES` (`Main,Default,Parallel,Cooperative,Worker`), `SCALE` (`X1`, `X4`, `X10`), `CPU_THROTTLING` (замедление главного потока,
воркеры DevTools не замедляет), `HEADFUL=1`, `OUTPUT` (путь к JSON).

## UI-сценарии на эмулированном телефоне

Скрипт сам раздаёт дистрибутив (`DIST_DIR`, по умолчанию `webApp/build/dist/wasmJs/productionExecutable`), открывает его
в headless Chrome с эмуляцией телефона (412×915, DPR 2.625, touch, Android UA) и на время каждого замера включает
замедление CPU. В трассе Chrome считает отрисовки Compose (колбэки `requestAnimationFrame` дольше 0,3 мс), долгие задачи,
стили и раскладку DOM (это зеркало a11y-дерева), GC; в странице — промежутки между кадрами, Long Animation Frames и
`pointercancel` (браузер забрал жест у Compose). Элементы находит через a11y-дерево Compose, поэтому a11y должен быть включён.

```bash
node tools/perf/run-ui-scenarios.mjs
```

Сценарии (`SCENARIOS`): `idle` и `idleHud` — 5 с простоя на Главной без HUD и с HUD; `checkIn` — тап по настроению и выезд
шторки; `tabs` — обход вкладок; `marketplaceScroll` — 6 жестов прокрутки Витрины (`Input.synthesizeScrollGesture`);
`carouselSwipe` — свайпы с плитки карусели, пары `dx:dy` в `SWIPES`, результат — что прокрутилось.

Переменные: `CPU_THROTTLING` (по умолчанию 4), `RUNS` (медиана, по умолчанию 3), `LABEL`, `OUTPUT`,
`CHROME_ARGS="--use-angle=swiftshader --enable-unsafe-swiftshader"` — программный GPU как грубая модель слабого
мобильного GPU (по умолчанию headless Chrome рисует аппаратно, через Metal), `DPR_CAP=2` — подменить `devicePixelRatio`,
`PROFILE_TOP=30` — CPU-профиль замера, самые тяжёлые функции (в production-wasm имён нет, видны WebGL-вызовы).

## Нативные функции

Нужен запущенный Chrome с портом отладки:

```bash
node tools/perf/cdp-cli.mjs launch /tmp/medex-native-profile
```

```bash
SAMPLE_FILE=/path/to/photo.png node tools/perf/native-check.mjs
```

Проверяет геолокацию (разрешено → запрещено → снова разрешено), буфер обмена, карту и `tel:`, выбор файла во время
заблокированного главного потока. Координаты кнопок рассчитаны на окно 1280×900 и экран «Возможности устройства».
Разрешения выставляются через `Browser.setPermission` и живут, пока открыта CDP-сессия, поэтому все шаги — в одном скрипте.

`cdp-cli.mjs` — разовые команды: `open`, `eval`, `click x y`, `wheel x y dy`, `shot file.png`, `click-upload x y file`, `geo lat lon`, `cpu rate`.
