# Use the official MySQL 8.0 image as the base
FROM mysql:8.0

COPY ./db_init/ /docker-entrypoint-initdb.d/

EXPOSE 3306
