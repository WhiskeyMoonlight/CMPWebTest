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
