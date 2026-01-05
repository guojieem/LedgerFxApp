@echo off
setlocal EnableDelayedExpansion

echo =========================================
echo LedgerFxApp - Windows EXE/MSI Builder
echo =========================================

REM ================== 配置 ==================
REM 使用双引号包裹路径，避免空格问题
set "JDK_HOME=C:\Program Files\java\jdk-17.0.16"
set "JAVAFX_HOME=C:\Program Files\javafx\javafx-sdk-20.0.2"
set "JAVAFX_LIB=%JAVAFX_HOME%\lib"

set "PROJECT_DIR=%~dp0"
set "TARGET_DIR=%PROJECT_DIR%target"
set "DIST_DIR=%PROJECT_DIR%dist"
set "RUNTIME_DIR=%PROJECT_DIR%runtime"

set "APP_NAME=LedgerFxApp"
set "JAR_NAME=LedgerFxApp-0.0.1.jar"
set "MAIN_CLASS=com.ledgerfx.LedgerFxApplication"
set "ICON_PATH=%PROJECT_DIR%src\main\resources\icons\app.ico"

REM ================== 清理 ==================
rmdir /s /q "%DIST_DIR%" 2>nul
rmdir /s /q "%RUNTIME_DIR%" 2>nul
mkdir "%DIST_DIR%"

REM ================== Maven 打包 ==================
echo Building Maven project...
call mvn clean package -Dmaven.test.skip=true
if %ERRORLEVEL% NEQ 0 exit /b 1

REM ================== jlink ==================
echo Creating custom runtime...
"%JDK_HOME%\bin\jlink" ^
--module-path "%JDK_HOME%\jmods;%JAVAFX_LIB%" ^
--add-modules ^
java.base,java.desktop,java.logging,java.sql,java.naming,jdk.crypto.ec,^
javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.swing ^
--strip-debug --no-header-files --no-man-pages --compress=2 ^
--output "%RUNTIME_DIR%"
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] jlink failed
    pause
    exit /b 1
)

REM ================== jpackage EXE ==================
echo Packaging EXE...
jpackage ^
--name "%APP_NAME%" ^
--input "%TARGET_DIR%" ^
--dest "%DIST_DIR%" ^
--main-jar "%JAR_NAME%" ^
--main-class "%MAIN_CLASS%" ^
--runtime-image "%RUNTIME_DIR%" ^
--type exe ^
--win-upgrade-uuid 9f3e2d44-2c4a-4f5e-a4e6-abc123456789 ^
--win-menu ^
--win-shortcut ^
--icon "%ICON_PATH%" ^
--vendor "LedgerFx" ^
--app-version 1.0.0 ^
--java-options "-Xmx512m --add-opens java.base/java.lang=ALL-UNNAMED -Dprism.verbose=true" ^
--verbose

REM ================== jpackage MSI ==================
echo Packaging MSI...
jpackage ^
--name "%APP_NAME%" ^
--input "%TARGET_DIR%" ^
--dest "%DIST_DIR%" ^
--main-jar "%JAR_NAME%" ^
--main-class "%MAIN_CLASS%" ^
--runtime-image "%RUNTIME_DIR%" ^
--type msi ^
--icon "%ICON_PATH%" ^
--win-menu ^
--win-shortcut ^
--vendor "LedgerFx" ^
--app-version 1.0.0 ^
--java-options "-Xmx512m --add-opens java.base/java.lang=ALL-UNNAMED" ^
--verbose

echo =========================================
echo ✅ BUILD SUCCESS
echo EXE/MSI generated in: %DIST_DIR%
echo =========================================
pause