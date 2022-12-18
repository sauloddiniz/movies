#FROM openjdk:13-alpine3.10
#ADD /target/movies-0.0.1-SNAPSHOT.jar app.jar
#EXPOSE 8090:8090
#ENTRYPOINT ["java","-jar", "app.jar"]

FROM openjdk:13-alpine3.10
#Crio um diretorio dentro da imagem onde vai receber os arquivos .jar etc..
WORKDIR /user/app
#Informo quuais arquivos devo copiar para dentro da imagem
COPY /target/movies-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090:8090
ENTRYPOINT ["java","-jar", "-Dspring.config.use-legacy-processing=true","app.jar"]