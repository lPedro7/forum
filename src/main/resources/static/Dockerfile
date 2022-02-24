FROM mysql
MAINTAINER csgeek@mail.com

ENV MYSQL_ROOT_PASSWORD root
ADD schema.sql /docker-entrypoint-initdb.d

EXPOSE 3306