# Escape de la Mazmorra

## Requisitos

- **Windows 10/11** con PowerShell.
- **JDK 21** instalado (Eclipse Temurin recomendado).
  - Descarga: https://adoptium.net/temurin/releases/?version=21
  - El lanzador detecta automaticamente el JDK en `JAVA_HOME`, el `PATH`, o en directorios comunes de instalacion. No es necesario configurar nada.
- **JavaFX 21.0.5** para Windows. Necesitas estos cuatro JAR en `lib\javafx\`:

  ```text
  lib/javafx/
    javafx-base-21.0.5-win.jar
    javafx-controls-21.0.5-win.jar
    javafx-graphics-21.0.5-win.jar
    javafx-media-21.0.5-win.jar
  ```

  Si faltan, descargalos desde https://gluonhq.com/products/javafx/ (JavaFX 21.0.5, Windows, SDK). Extrae solo los JAR listados y copialos a `lib\javafx\`.

## Contenido del ZIP (entrega)

```
EscapeMazmorra.bat          <-- Lanzador principal (doble clic)
scripts/
    run.ps1                 <-- Compila y ejecuta el juego
    test.ps1                <-- Compila y ejecuta los tests
src/                        <-- Codigo fuente
test/                       <-- Tests JUnit
lib/
    gson-2.10.1.jar         <-- Gson para JSON
    junit-platform-console-standalone-1.10.0.jar  <-- JUnit (solo tests)
    javafx/                 <-- JAR de JavaFX (ver requisitos)
datos/
    cuevas.json             <-- Configuracion del juego
```

No incluir `build/`, `out/` ni archivos temporales.

## Ejecutar el juego (recomendado)

Haz doble clic en:

```text
EscapeMazmorra.bat
```

El lanzador busca automaticamente el JDK 21, compila el codigo y abre la ventana del juego.

## Ejecutar desde PowerShell

```powershell
powershell.exe -ExecutionPolicy Bypass -File scripts\run.ps1
```

## Solo compilar (sin abrir ventana)

```powershell
powershell.exe -ExecutionPolicy Bypass -File scripts\run.ps1 -CompileOnly
```

## Ejecutar tests

```powershell
powershell.exe -ExecutionPolicy Bypass -File scripts\test.ps1
```

## Notas

- La ventana de juego se mantiene fija a 1280x720 para evitar errores visuales al maximizar o usar pantalla completa en algunos equipos.
- `datos/cuevas.json` debe estar presente porque contiene la configuracion del juego.
- Si el `.bat` no encuentra el JDK, muestra un mensaje con el enlace de descarga de Adoptium JDK 21.
