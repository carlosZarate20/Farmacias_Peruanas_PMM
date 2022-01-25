FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.6_10
RUN apk add --no-cache tzdata
ENV TZ='America/Lima'
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN apk --update add fontconfig ttf-dejavu
WORKDIR /
RUN mkdir app && chmod 777 app
COPY target/fps-expc-ms-com.farmaciasperuanas.pmmli.merchandisemovements-merchandisemovements-1.0-SNAPSHOT.jar /app

ADD devops/newrelic/newrelic.jar /app
ADD devops/newrelic/newrelic.yml /app

ENV NEW_RELIC_APP_NAME="fps-expc-ms-com.farmaciasperuanas.pmmli.merchandisemovements-merchandisemovements"
ENV NEW_RELIC_LICENSE_KEY="license_key"
ENV NEW_RELIC_LOG_FILE_NAME="STDOUT"

WORKDIR /app
EXPOSE 8080
CMD ["java", "-javaagent:/app/newrelic.jar","-jar","-Xmx700M", "fps-expc-ms-com.farmaciasperuanas.pmmli.merchandisemovements-merchandisemovements-1.0-SNAPSHOT.jar"]