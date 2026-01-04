@echo off
setlocal

REM ----------------------------
REM 配置区域，请根据你的环境修改
REM ----------------------------

REM JavaFX SDK 路径
set JAVAFX_PATH=C:\javafx-sdk-20.0.2\lib

REM Maven 项目路径（当前目录即可）
set PROJECT_DIR=%CD%

REM Maven 目标目录
set TARGET_DIR=%PROJECT_DIR%\target

REM 主 JAR 文件名（Spring Boot 可执行 JAR）
set JAR_NAME=LedgerFxApp-1.0-SNAPSHOT.jar

REM 主类（启动类）
set MAIN_CLASS=com.ledgerfx.LedgerFxApplication

REM 输出 EXE 名称
set APP_NAME=LedgerFxApp

REM 可选 ICO 图标路径
set ICON_PATH=%PROJECT_DIR%\resources\icons\app.ico

REM ----------------------------
REM 步骤 1：清理 + Maven 打包
REM ----------------------------
echo.
echo ==========================
echo Step 1: Maven clean package
echo ==========================
mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo Maven build failed! Aborting.
    exit /b 1
)

REM ----------------------------
REM 步骤 2：生成 EXE
REM ----------------------------
echo.
echo ==========================
echo Step 2: Generating EXE with jpackage
echo ==========================
jpackage ^
--name %APP_NAME% ^
--input %TARGET_DIR% ^
--main-jar %JAR_NAME% ^
--main-class %MAIN_CLASS% ^
--type exe ^
--icon %ICON_PATH% ^
--module-path "%JAVAFX_PATH%" ^
--add-modules javafx.controls,javafx.fxml,javafx.swing ^
--win-menu ^
--win-dir-chooser ^
--win-shortcut ^
--verbose

if %ERRORLEVEL% NEQ 0 (
    echo jpackage failed! Aborting.
    exit /b 1
)

REM ----------------------------
REM 步骤 3（可选）：生成 MSI 安装包
REM ----------------------------
echo.
echo ==========================
echo Step 3: Generating MSI installer (optional)
echo ==========================
jpackage ^
--name %APP_NAME% ^
--input %TARGET_DIR% ^
--main-jar %JAR_NAME% ^
--main-class %MAIN_CLASS% ^
--type msi ^
--icon %ICON_PATH% ^
--module-path "%JAVAFX_PATH%" ^
--add-modules javafx.controls,javafx.fxml,javafx.swing ^
--win-menu ^
--win-shortcut ^
--verbose

if %ERRORLEVEL% NEQ 0 (
    echo MSI build failed!
    REM MSI 可选，不影响 EXE
)

echo.
echo ==========================
echo Build complete!
echo EXE and MSI (optional) generated.
echo ==========================
pause