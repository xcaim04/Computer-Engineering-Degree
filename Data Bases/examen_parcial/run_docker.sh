#!/bin/bash

# Configuración
IMAGE_NAME="parcialdb"
CONTAINER_NAME="parcialdb"
POSTGRES_PASSWORD="Carlos1234*"
DB_NAME="employeesdb"
HOST_PORT=5432
CONTAINER_PORT=5432

# Detener y eliminar el contenedor si ya existe
if [ "$(docker ps -aq -f name=${CONTAINER_NAME})" ]; then
    echo "🔹 Eliminando contenedor existente: ${CONTAINER_NAME}"
    docker stop ${CONTAINER_NAME} 2>/dev/null
    docker rm ${CONTAINER_NAME} 2>/dev/null
fi

# Construir la imagen
echo "🔹 Construyendo imagen: ${IMAGE_NAME}"
docker build -t ${IMAGE_NAME} .

# Ejecutar el contenedor
echo "🔹 Iniciando contenedor: ${CONTAINER_NAME}"
docker run -d \
    --name ${CONTAINER_NAME} \
    -p ${HOST_PORT}:${CONTAINER_PORT} \
    -e POSTGRES_PASSWORD="${POSTGRES_PASSWORD}" \
    -e POSTGRES_DB=${DB_NAME} \
    ${IMAGE_NAME}

# Esperar a que PostgreSQL esté listo
echo "🔹 Esperando a que la base de datos esté disponible..."
sleep 5

# Mostrar logs recientes (opcional)
echo "🔹 Últimos logs del contenedor:"
docker logs --tail 20 ${CONTAINER_NAME}

# Comprobar si el contenedor está corriendo
if [ "$(docker ps -q -f name=${CONTAINER_NAME})" ]; then
    echo "✅ Contenedor ${CONTAINER_NAME} está corriendo."
    echo "Para conectarte a psql ejecuta:"
    echo "  docker exec -it ${CONTAINER_NAME} psql -U postgres -d ${DB_NAME}"
else
    echo "❌ El contenedor falló al iniciar. Revisa los logs con: docker logs ${CONTAINER_NAME}"
    exit 1
fi