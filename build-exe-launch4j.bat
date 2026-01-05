@echo off
setlocal EnableDelayedExpansion

echo =========================================
echo LedgerFxApp - Launch4j One-Click Builder
echo =========================================
echo.

REM ==================================================
REM 【1】基础配置（只需要改这里）
REM ==================================================

REM 应用信息
set APP_NAME=LedgerFxApp
set JAR_NAME=LedgerFxApp-0.0.1.jar

REM 项目路径
set PROJECT_DIR=%~dp0
set TARGET_DIR=%PROJECT_DIR%target
set DIST_DIR=%PROJECT_DIR%dist

REM Launch4j 路径
set LAUNCH4J=C:\Launch4j\launch4jc.exe

REM 系统 JDK（作为内置 JDK 拷贝源）
set SYSTEM_JDK=C:\Program Files\Java\jdk-17.0.16

REM ==================================================
REM 【2】环境检查
REM ==================================================

if not exist "%LAUNCH4J%" (
    echo [ERROR] Launch4j not found:
    echo %LAUNCH4J%
    pause
    exit /b 1
)

if not exist "%SYSTEM_JDK%\bin\java.exe" (
    echo [ERROR] System JDK not found:
    echo %SYSTEM_JDK%
    pause
    exit /b 1
)

REM ==================================================
REM 【3】清理 & 目录准备
REM ==================================================

echo [1/6] Cleaning old dist...
rmdir /s /q "%DIST_DIR%" 2>nul

mkdir "%DIST_DIR%"
mkdir "%DIST_DIR%\.app"

REM ==================================================
REM 【4】Maven 打包 JAR
REM ==================================================

echo [2/6] Building JAR with Maven...
call mvn clean package -Dmaven.test.skip=true
if errorlevel 1 (
    echo [ERROR] Maven build failed
    pause
    exit /b 1
)

REM ==================================================
REM 【5】拷贝 JAR 到隐藏目录
REM ==================================================

echo [3/6] Copying application JAR...
copy "%TARGET_DIR%\%JAR_NAME%" "%DIST_DIR%\.app\" >nul

if not exist "%DIST_DIR%\.app\%JAR_NAME%" (
    echo [ERROR] JAR not found after copy:
    echo %DIST_DIR%\.app\%JAR_NAME%
    pause
    exit /b 1
)

REM ==================================================
REM 【6】拷贝内置 JDK
REM ==================================================

echo [4/6] Copying bundled JDK (this may take a while)...
xcopy "%SYSTEM_JDK%" "%DIST_DIR%\jre\" /E /I /H /Y >nul

if not exist "%DIST_DIR%\jre\bin\java.exe" (
    echo [ERROR] Bundled JDK copy failed
    pause
    exit /b 1
)

REM ==================================================
REM 【7】生成 EXE（Launch4j）
REM ==================================================

echo [5/6] Building EXE with Launch4j...
"%LAUNCH4J%" launch4j.xml
if errorlevel 1 (
    echo [ERROR] Launch4j failed
    pause
    exit /b 1
)

REM ==================================================
REM 【8】完成
REM ==================================================

echo.
echo =========================================
echo ✅ BUILD SUCCESS
echo.
echo Output directory:
echo %DIST_DIR%
echo.
echo Files:
echo  - LedgerFxApp.exe
echo  - jre\ (bundled JDK)
echo  - .app\LedgerFxApp.jar (hidden)
echo =========================================
echo.

pause
