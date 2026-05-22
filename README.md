# Escape de la Mazmorra

## Ejecutar el juego

Desde PowerShell, en la raiz del proyecto:

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

El script usa el JDK 21 configurado en `C:\Users\UAH\.jdks\ms-21.0.10` si `JAVA_HOME` no apunta a un JDK con `javac`.
Tambien espera encontrar JavaFX 21.0.5 en el repositorio Maven local (`.m2`), tal como esta configurado en IntelliJ.
