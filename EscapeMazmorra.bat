@echo off
setlocal enabledelayedexpansion

set "JAVAC="

REM 1. JAVA_HOME
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\javac.exe" set "JAVAC=%JAVA_HOME%\bin\javac.exe" & goto run

REM 2. PATH
where javac.exe 2>nul >nul
if !errorlevel! equ 0 for /f "delims=" %%i in ('where javac.exe') do set "JAVAC=%%i" & goto run

REM 3. Directorios comunes de instalacion
for /f "delims=" %%d in ('dir "C:\Program Files\Java\jdk*" /b /ad 2^>nul') do if exist "C:\Program Files\Java\%%d\bin\javac.exe" set "JAVAC=C:\Program Files\Java\%%d\bin\javac.exe" & goto run
for /f "delims=" %%d in ('dir "C:\Program Files\Eclipse Adoptium\jdk*" /b /ad 2^>nul') do if exist "C:\Program Files\Eclipse Adoptium\%%d\bin\javac.exe" set "JAVAC=C:\Program Files\Eclipse Adoptium\%%d\bin\javac.exe" & goto run
for /f "delims=" %%d in ('dir "C:\Program Files\Microsoft\jdk*" /b /ad 2^>nul') do if exist "C:\Program Files\Microsoft\%%d\bin\javac.exe" set "JAVAC=C:\Program Files\Microsoft\%%d\bin\javac.exe" & goto run
for /f "delims=" %%d in ('dir "%LOCALAPPDATA%\Programs\Eclipse Adoptium\jdk*" /b /ad 2^>nul') do if exist "%LOCALAPPDATA%\Programs\Eclipse Adoptium\%%d\bin\javac.exe" set "JAVAC=%LOCALAPPDATA%\Programs\Eclipse Adoptium\%%d\bin\javac.exe" & goto run
for /f "delims=" %%d in ('dir "%USERPROFILE%\.jdks\*" /b /ad 2^>nul') do if exist "%USERPROFILE%\.jdks\%%d\bin\javac.exe" set "JAVAC=%USERPROFILE%\.jdks\%%d\bin\javac.exe" & goto run

REM 4. Registro de Windows
for /f "skip=2 tokens=1 delims=" %%i in ('reg query "HKLM\SOFTWARE\JavaSoft\JDK" /v JavaHome /s 2^>nul') do (
    for /f "tokens=3,*" %%a in ("%%i") do (
        if exist "%%a %%b\bin\javac.exe" set "JAVAC=%%a %%b\bin\javac.exe" & goto run
    )
)

REM No encontrado
echo.
echo ERROR: No se encontro javac.exe. Instala JDK 21 desde:
echo        https://adoptium.net/temurin/releases/?version=21
echo.
echo O configura JAVA_HOME apuntando al JDK 21.
echo.
pause
exit /b 1

:run
for %%i in ("%JAVAC%") do set "JAVA_HOME=%%~dpi.."
echo Usando JDK: %JAVA_HOME%
echo.
powershell -ExecutionPolicy Bypass -File "%~dp0scripts\run.ps1"
if %errorlevel% neq 0 pause
