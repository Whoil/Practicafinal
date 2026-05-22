param(
    [switch]$CompileOnly
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$ProjectRoot = Split-Path -Parent $PSScriptRoot
$BuildDir = Join-Path $ProjectRoot "build\classes"
$SourcesFile = Join-Path $ProjectRoot "build\sources.txt"

$DefaultJavaHome = "C:\Users\UAH\.jdks\ms-21.0.10"
$JavaHome = $DefaultJavaHome
if ($env:JAVA_HOME -and (Test-Path (Join-Path $env:JAVA_HOME "bin\javac.exe"))) {
    $JavaHome = $env:JAVA_HOME
}
$Javac = Join-Path $JavaHome "bin\javac.exe"
$Java = Join-Path $JavaHome "bin\java.exe"

$GsonJar = Join-Path $ProjectRoot "lib\gson-2.10.1.jar"
$JavaFxJars = @(
    "$env:USERPROFILE\.m2\repository\org\openjfx\javafx-base\21.0.5\javafx-base-21.0.5-win.jar",
    "$env:USERPROFILE\.m2\repository\org\openjfx\javafx-controls\21.0.5\javafx-controls-21.0.5-win.jar",
    "$env:USERPROFILE\.m2\repository\org\openjfx\javafx-graphics\21.0.5\javafx-graphics-21.0.5-win.jar"
)

if (!(Test-Path $Javac)) {
    throw "No se encontro javac en $Javac. Revisa JAVA_HOME o DefaultJavaHome en scripts\run.ps1."
}

if (!(Test-Path $Java)) {
    throw "No se encontro java en $Java. Revisa JAVA_HOME o DefaultJavaHome en scripts\run.ps1."
}

if (!(Test-Path $GsonJar)) {
    throw "No se encontro Gson en $GsonJar."
}

foreach ($Jar in $JavaFxJars) {
    if (!(Test-Path $Jar)) {
        throw "No se encontro JavaFX en $Jar. Revisa la version o instala OpenJFX 21.0.5."
    }
}

New-Item -ItemType Directory -Force -Path $BuildDir | Out-Null
$SourcePaths = Get-ChildItem -Path (Join-Path $ProjectRoot "src") -Recurse -Filter "*.java" |
    ForEach-Object { '"' + ($_.FullName -replace "\\", "/") + '"' }
$Utf8NoBom = New-Object System.Text.UTF8Encoding($false)
[System.IO.File]::WriteAllLines($SourcesFile, $SourcePaths, $Utf8NoBom)

$JavaFxPath = $JavaFxJars -join ";"
$CompileClasspath = $GsonJar
$RunClasspath = "$BuildDir;$GsonJar"

Write-Host "Compilando Escape de la Mazmorra..."
& $Javac `
    -encoding UTF-8 `
    --module-path $JavaFxPath `
    --add-modules javafx.controls `
    -cp $CompileClasspath `
    -d $BuildDir `
    "@$SourcesFile"

if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

if ($CompileOnly) {
    Write-Host "Compilacion completada."
    exit 0
}

Write-Host "Lanzando Escape de la Mazmorra..."
& $Java `
    --module-path $JavaFxPath `
    --add-modules javafx.controls `
    -cp $RunClasspath `
    Main
