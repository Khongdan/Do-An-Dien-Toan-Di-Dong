@echo off
:: 1. Bien dich chuong trinh (Buoc nay van hien Terminal tam thoi de ban biet co loi hay khong)
javac -d bin -sourcepath src src/server/ServerApp.java src/client/ClientApp.java

if %errorlevel% neq 0 (
    echo [LOI] Khong the bien dich. Vui long kiem tra code!
    pause
    exit /b
)

:: 2. Khoi chay Server ma khong hien Terminal
start /b javaw -cp bin server.ServerApp

:: 3. Khoi chay Client ma khong hien Terminal
start /b javaw -cp bin client.ClientApp

:: 4. Thoat file bat ngay lap tuc
exit