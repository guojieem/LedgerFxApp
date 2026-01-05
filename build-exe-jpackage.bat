@echo off
setlocal

REM ========= 基础配置 =========
set APP_NAME=LedgerFxApp
set APP_VERSION=1.0.0
set JAR_NAME=LedgerFxApp-0.0.1.jar
set MAIN_CLASS=com.ledgerfx.LedgerFxApplication

set PROJECT_DIR=%~dp0
set TARGET_DIR=%PROJECT_DIR%target
set DIST_DIR=%PROJECT_DIR%dist-jpackage

set JAVAFX_HOME=C:\Program Files\JavaFX\javafx-sdk-20.0.2
set JAVAFX_LIB=%JAVAFX_HOME%\lib

REM ========= 清理 =========
rmdir /s /q "%DIST_DIR%" 2>nul
mkdir "%DIST_DIR%"

REM ========= Maven 打包 =========
call mvn clean package -Dmaven.test.skip=true
if errorlevel 1 exit /b 1

REM ========= jpackage =========
jpackage ^
--type exe ^
--name %APP_NAME% ^
--app-version %APP_VERSION% ^
--input "%TARGET_DIR%" ^
--main-jar %JAR_NAME% ^
--main-class %MAIN_CLASS% ^
--dest "%DIST_DIR%" ^
--java-options "--module-path=%JAVAFX_LIB% --add-modules=javafx.controls,javafx.fxml,javafx.graphics -Dprism.order=sw -Dprism.forceGPU=false" ^
--win-menu ^
--win-shortcut ^
--icon src\main\resources\icons\app.ico ^
--verbose

pause
