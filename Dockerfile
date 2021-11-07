FROM openjdk:8-jre-alpine

RUN apk add --no-cache ttf-liberation  # agrego fuentes aveces si trabajo con ireport da muchos problemas
RUN mkdir -p /usr/src/app  #donde se almacanara el archivo jar en el contenedor

#se aplica una ruta de trabajo
WORKDIR /usr/src/app

#se aplica la zona horaria
RUN apk add --no-cache tzdata
ENV TZ=America/Lima
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

#copio el archio jar del proyecto y lo pongo en el directorio de trabajo
COPY target/taller_sba-0.0.1-SNAPSHOT.jar /usr/src/app/app.jar

EXPOSE 8080

#se ejecuta y activo el modo produccion
CMD java -jar -Dspring.profiles.active=prod app.jar