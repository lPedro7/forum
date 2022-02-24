FROM mysql

ENV MYSQL_DATABASE=forum \
    MYSQL_ROOT_PASSWORD=root

ADD schema.sql /docker-entrypoint-initdb.d

EXPOSE 3306