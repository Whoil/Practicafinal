Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

# Auto-detect JDK si no esta configurado o no es valido
if (-not $env:JAVA_HOME -or -not (Test-Path (Join-Path $env:JAVA_HOME "bin\javac.exe"))) {
    $searchDirs = @(
        "$env:ProgramFiles\Java",
        "$env:ProgramFiles\Eclipse Adoptium",
        "$env:ProgramFiles\Microsoft",
        "$env:LocalAppData\Programs\Eclipse Adoptium",
        "$env:USERPROFILE\.jdks"
    )
    :outer foreach ($base in $searchDirs) {
        $jdks = Get-ChildItem -Path "$base\*" -Directory -ErrorAction SilentlyContinue |
            Where-Object { $_.Name -like "jdk*" }
        foreach ($jdk in $jdks) {
            if (Test-Path (Join-Path $jdk.FullName "bin\javac.exe")) {
                $env:JAVA_HOME = $jdk.FullName
                break outer
            }
        }
    }
}

$ProjectRoot = Split-Path -Parent $PSScriptRoot
$BuildDir = Join-Path $ProjectRoot "build\test-classes"
$SourcesFile = Join-Path $ProjectRoot "build\test-sources.txt"

function Resolve-Executable([string]$Name) {
    $Command = Get-Command $Name -ErrorAction SilentlyContinue
    if ($Command) {
        return $Command.Source
    }
    return $null
}

if ($env:JAVA_HOME -and (Test-Path (Join-Path $env:JAVA_HOME "bin\javac.exe"))) {
    $Javac = Join-Path $env:JAVA_HOME "bin\javac.exe"
    $Java = Join-Path $env:JAVA_HOME "bin\java.exe"
} else {
    $Javac = Resolve-Executable "javac.exe"
    $Java = Resolve-Executable "java.exe"
}

$GsonJar = Join-Path $ProjectRoot "lib\gson-2.10.1.jar"
$JunitJar = Join-Path $ProjectRoot "lib\junit-platform-console-standalone-1.10.0.jar"
$LocalJavaFxDir = Join-Path $ProjectRoot "lib\javafx"
$LocalJavaFxJars = @(
    (Join-Path $LocalJavaFxDir "javafx-base-21.0.5-win.jar")
    (Join-Path $LocalJavaFxDir "javafx-controls-21.0.5-win.jar")
    (Join-Path $LocalJavaFxDir "javafx-graphics-21.0.5-win.jar")
    (Join-Path $LocalJavaFxDir "javafx-media-21.0.5-win.jar")
)
$MavenJavaFxJars = @(
    "$env:USERPROFILE\.m2\repository\org\openjfx\javafx-base\21.0.5\javafx-base-21.0.5-win.jar",
    "$env:USERPROFILE\.m2\repository\org\openjfx\javafx-controls\21.0.5\javafx-controls-21.0.5-win.jar",
    "$env:USERPROFILE\.m2\repository\org\openjfx\javafx-graphics\21.0.5\javafx-graphics-21.0.5-win.jar",
    "$env:USERPROFILE\.m2\repository\org\openjfx\javafx-media\21.0.5\javafx-media-21.0.5-win.jar"
)
$JavaFxJars = $MavenJavaFxJars
$MissingLocalJavaFxJars = @($LocalJavaFxJars | Where-Object { !(Test-Path $_) })
if ($MissingLocalJavaFxJars.Count -eq 0) {
    $JavaFxJars = $LocalJavaFxJars
}

if (!$Javac -or !(Test-Path $Javac)) {
    throw "No se encontro javac. Instala JDK 21 y configura JAVA_HOME o anade javac al PATH."
}

if (!$Java -or !(Test-Path $Java)) {
    throw "No se encontro java. Instala JDK 21 y configura JAVA_HOME o anade java al PATH."
}

foreach ($Jar in @($GsonJar, $JunitJar) + $JavaFxJars) {
    if (!(Test-Path $Jar)) {
        throw "No se encontro dependencia en $Jar. Para JavaFX, instala OpenJFX en Maven local o copia sus JAR win en lib\javafx."
    }
}

New-Item -ItemType Directory -Force -Path $BuildDir | Out-Null
$SourcePaths = @()
$SourcePaths += Get-ChildItem -Path (Join-Path $ProjectRoot "src") -Recurse -Filter "*.java" |
    ForEach-Object { '"' + ($_.FullName -replace "\\", "/") + '"' }
$SourcePaths += Get-ChildItem -Path (Join-Path $ProjectRoot "test") -Recurse -Filter "*.java" |
    ForEach-Object { '"' + ($_.FullName -replace "\\", "/") + '"' }
$Utf8NoBom = New-Object System.Text.UTF8Encoding($false)
[System.IO.File]::WriteAllLines($SourcesFile, $SourcePaths, $Utf8NoBom)

$JavaFxPath = $JavaFxJars -join ";"
$CompileClasspath = "$GsonJar;$JunitJar"
$RunClasspath = "$BuildDir;$GsonJar"

Write-Host "Compilando codigo y tests..."
& $Javac `
    -encoding UTF-8 `
    --module-path $JavaFxPath `
    --add-modules javafx.controls,javafx.media `
    -cp $CompileClasspath `
    -d $BuildDir `
    "@$SourcesFile"

if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

Write-Host "Ejecutando tests..."
& $Java `
    --module-path $JavaFxPath `
    --add-modules javafx.controls,javafx.media `
    -jar $JunitJar `
    execute `
    --class-path $RunClasspath `
    --scan-class-path $BuildDir `
    --details summary

exit $LASTEXITCODE
