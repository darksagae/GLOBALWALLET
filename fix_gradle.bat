@echo off
echo ========================================
echo    Global Wallet - Gradle Fix Tool
echo ========================================
echo.

echo Step 1: Clearing Gradle cache...
if exist "%USERPROFILE%\.gradle" (
    echo Deleting existing Gradle cache...
    rmdir /s /q "%USERPROFILE%\.gradle"
    echo Gradle cache cleared!
) else (
    echo No existing Gradle cache found.
)

echo.
echo Step 2: Creating new Gradle directory...
if not exist "C:\Gradle" (
    mkdir "C:\Gradle"
    echo Created C:\Gradle directory
) else (
    echo C:\Gradle directory already exists
)

echo.
echo Step 3: Setting up environment...
echo Please manually set these environment variables:
echo.
echo GRADLE_HOME = C:\Gradle\gradle-8.4
echo Add to PATH: %%GRADLE_HOME%%\bin
echo.
echo To set environment variables:
echo 1. Press Win + R
echo 2. Type: sysdm.cpl
echo 3. Click "Environment Variables"
echo 4. Add the variables above
echo.

echo Step 4: Next steps...
echo 1. Download Gradle 8.4 from: https://gradle.org/releases/
echo 2. Extract to: C:\Gradle\gradle-8.4
echo 3. Open Android Studio as Administrator
echo 4. Open the project and sync
echo.

echo ========================================
echo    Fix completed! 
echo ========================================
pause 