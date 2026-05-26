# Escape de la Mazmorra

## Requisitos

- Windows con PowerShell.
- JDK 21 instalado.
- `JAVA_HOME` apuntando al JDK 21, o `java` y `javac` disponibles en el `PATH`.
- JavaFX 21.0.5 para Windows. El script lo busca en este orden:
  1. `lib\javafx\` dentro del proyecto.
  2. Repositorio Maven local del usuario: `%USERPROFILE%\.m2\repository\org\openjfx\...`.

Si el juego se abre en otro ordenador y falta JavaFX, copia en `lib\javafx\` estos cuatro JAR:

```text
javafx-base-21.0.5-win.jar
javafx-controls-21.0.5-win.jar
javafx-graphics-21.0.5-win.jar
javafx-media-21.0.5-win.jar
```

## Ejecutar el juego

La forma recomendada es hacer doble clic en:

```text
EscapeMazmorra.bat
```

Ese lanzador compila y abre el juego automaticamente usando `scripts\run.ps1`.

Si se prefiere ejecutar manualmente desde PowerShell, en la raiz del proyecto:

```powershell
powershell.exe -ExecutionPolicy Bypass -File scripts\run.ps1
```

## Solo compilar

```powershell
powershell.exe -ExecutionPolicy Bypass -File scripts\run.ps1 -CompileOnly
```

## Ejecutar tests

```powershell
powershell.exe -ExecutionPolicy Bypass -File scripts\test.ps1
```

## Notas para entrega

- No es necesario subir `build/`, `out/` ni archivos temporales de ejecucion.
- `datos/cuevas.json` debe estar presente porque contiene la configuracion del juego.
- `lib\gson-2.10.1.jar` y `lib\junit-platform-console-standalone-1.10.0.jar` deben conservarse.
- La ventana de juego se mantiene fija a 1280x720 para evitar errores visuales al maximizar o usar pantalla completa en algunos equipos.
