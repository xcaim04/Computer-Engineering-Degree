@echo off
setlocal enabledelayedexpansion

:: Configuración
set IMAGE_NAME=parcialdb
set CONTAINER_NAME=parcialdb
set POSTGRES_PASSWORD=Carlos1234*
set DB_NAME=employeesdb
set HOST_PORT=5432
set CONTAINER_PORT=5432

:: Detener y eliminar contenedor si existe
docker ps -q -f name=%CONTAINER_NAME% >nul
if %errorlevel% equ 0 (
    echo 🔹 Deteniendo contenedor %CONTAINER_NAME%
    docker stop %CONTAINER_NAME%
)
docker ps -aq -f name=%CONTAINER_NAME% >nul
if %errorlevel% equ 0 (
    echo 🔹 Eliminando contenedor %CONTAINER_NAME%
    docker rm %CONTAINER_NAME%
)

:: Construir imagen
echo 🔹 Construyendo imagen %IMAGE_NAME%
docker build -t %IMAGE_NAME% .

:: Ejecutar contenedor
echo 🔹 Iniciando contenedor %CONTAINER_NAME%
docker run -d --name %CONTAINER_NAME% -p %HOST_PORT%:%CONTAINER_PORT% -e POSTGRES_PASSWORD=%POSTGRES_PASSWORD% -e POSTGRES_DB=%DB_NAME% %IMAGE_NAME%

:: Esperar unos segundos
echo 🔹 Esperando a que la base de datos esté disponible...
timeout /t 5 /nobreak >nul

:: Mostrar logs recientes
echo 🔹 Últimos logs:
docker logs --tail 20 %CONTAINER_NAME%

:: Verificar si el contenedor está corriendo
docker ps -q -f name=%CONTAINER_NAME% >nul
if %errorlevel% equ 0 (
    echo ✅ Contenedor %CONTAINER_NAME% está corriendo.
    echo Para conectarte a psql ejecuta:
    echo   docker exec -it %CONTAINER_NAME% psql -U postgres -d %DB_NAME%
) else (
    echo ❌ El contenedor falló al iniciar. Revisa logs con: docker logs %CONTAINER_NAME%
    exit /b 1
)

endlocal