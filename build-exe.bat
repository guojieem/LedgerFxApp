@echo off
setlocal

REM ========= 配置 =========
set APP_NAME=LedgerFxApp
set JAR_NAME=LedgerFxApp-0.0.1.jar

set PROJECT_DIR=%~dp0
set TARGET_DIR=%PROJECT_DIR%target
set DIST_DIR=%PROJECT_DIR%dist

set SYSTEM_JDK=C:\Program Files\Java\jdk-17.0.16
set LAUNCH4J=D:\Program Files (x86)\Launch4j\launch4jc.exe

REM ========= 清理 =========
rmdir /s /q "%DIST_DIR%" 2>nul
mkdir "%DIST_DIR%"
mkdir "%DIST_DIR%\.app"

REM ========= Maven =========
call mvn clean package -Dmaven.test.skip=true
if errorlevel 1 exit /b 1

copy "%TARGET_DIR%\%JAR_NAME%" "%DIST_DIR%\.app\" >nul

REM ========= 内置 JDK =========
xcopy "%SYSTEM_JDK%" "%DIST_DIR%\jre\" /E /I /H /Y >nul

REM ========= Launch4j =========
"%LAUNCH4J%" launch4j.xml

echo.
echo ✅ EXE 已生成：dist\LedgerFxApp.exe
pause
