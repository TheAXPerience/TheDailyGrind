# What is This?
The Daily Grind is a web application that allows you to write down events and journal entries specific to certain dates and times, and search for them by date and time. The program can be used by many users, and events can be set public so others can see what you have up ahead in your schedule.

The Daily Grind was built using Java Spring Boot using Java 17.

# How do I use it?
## Set up a MySQL instance
The Daily Grind operates using a MySQL instance as the backend database server. Set up a MySQL instance on Docker so that it can be connected to the program. Check out the official MySQL image at https://hub.docker.com/_/mysql/ for more information.

## Using a ```docker-compose.yml``` file
The Daily Grind can also be set up in conjunction with the MySQL instance via Docker Compose.

Example ```docker-compose.yml``` file:

```
version: '3.1'

services:
  db:
    image: mysql
    restart: always
    ports:
      - 3306:3306
    volumes:
      - mysql:/var/lib/mysql
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: thedailygrind
      MYSQL_USER: mysql_usr
      MYSQL_PASSWORD: mysql_pwd
    healthcheck:
      test: ["CMD", "mysqladmin" ,"ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  adminer:
    image: adminer
    restart: always
    environment:
      ADMINER_DEFAULT_SERVER: db
    ports:
      - 8081:8080

  thedailygrind:
    container_name: app
    image: docker.io/axperience/thedailygrind:0.0.1
    build:
      context: .
    environment:
    - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/thedailygrind
    - SPRING_DATASOURCE_USERNAME=mysql_usr
    - SPRING_DATASOURCE_PASSWORD=mysql_pwd
    - SPRING_JPA_HIBERNATE_DDL_AUTO=update
    ports:
    - "3000:3000"
    depends_on:
      db:
        condition: service_healthy

volumes:
  mysql:
```
