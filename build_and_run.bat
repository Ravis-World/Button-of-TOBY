@echo off
setlocal enabledelayedexpansion

echo =========================================
echo  Building button-of-toby project
echo =========================================

echo Running Maven clean test...
call mvn -f button-of-toby\pom.xml clean test
if errorlevel 1 (
    echo.
    echo [ERROR] Maven build or tests failed. Application will not launch.
    pause
    exit /b 1
)

set SRC_DIR=button-of-toby\src\main\java
set OUT_DIR=bin
set JAR_NAME=button-of-toby.jar
set MAIN_CLASS=com.ravi.Main
set SOURCES_FILE=sources.txt

:: 1. Clean previous build directory, JAR, and sources file
if exist %OUT_DIR% rmdir /s /q %OUT_DIR%
if exist %JAR_NAME% del /f /q %JAR_NAME%
if exist %SOURCES_FILE% del /f /q %SOURCES_FILE%

mkdir %OUT_DIR%

:: 2. Find all Java source files cleanly without escaping slashes
echo Gathering Java source files...
for /f "delims=" %%F in ('dir /s /b "%SRC_DIR%\*.java"') do (
    set "FILEPATH=%%F"
    set "FILEPATH=!FILEPATH:\=/!"
    echo "!FILEPATH!">> %SOURCES_FILE%
)

:: Verify sources.txt was generated
if not exist %SOURCES_FILE% (
    echo [ERROR] No Java files found in %SRC_DIR%!
    pause
    exit /b 1
)

:: 3. Compile Java sources
echo Compiling Java code...
javac -d %OUT_DIR% @%SOURCES_FILE%
if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] Compilation failed! Check the errors above.
    if exist %SOURCES_FILE% del /f /q %SOURCES_FILE%
    pause
    exit /b %ERRORLEVEL%
)

:: Clean up temp file after successful compile
del /f /q %SOURCES_FILE%

:: 4. Copy resources/assets if present
if exist button-of-toby\src\main\resources (
    echo Copying resources...
    xcopy /s /e /y /q button-of-toby\src\main\resources\* %OUT_DIR%\
)

:: 5. Build Executable JAR File
echo Creating JAR file...
jar --create --file=%JAR_NAME% --main-class=%MAIN_CLASS% -C %OUT_DIR% .
if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] Failed to create JAR file!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo [SUCCESS] Build completed successfully: %JAR_NAME%
echo =========================================
echo Launching Application...
echo =========================================
echo.

:: 6. Run the generated JAR
java -jar %JAR_NAME%