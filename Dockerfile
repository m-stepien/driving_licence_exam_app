FROM postgres

ENV POSTGRES_DB=driving_exam
ENV POSTGRES_USER=dev1
ENV POSTGRES_PASSWORD=development

EXPOSE 5432

COPY src/main/resources/sql/init_table01.sql /docker-entrypoint-initdb.d/