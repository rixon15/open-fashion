# Use the official MySQL 8.0 image as the base
FROM mysql:8.0

# Copy the initialization scripts into the Docker image
# These scripts will be executed automatically by the MySQL entrypoint
# on the first run of the container.
COPY ./db_init/ /docker-entrypoint-initdb.d/

# Expose the default MySQL port
EXPOSE 3306
