FROM node:18-alpine
VOLUME /tmp
EXPOSE 8080
#ADD /build/libs/<jar_name.jar> <docker_jar_name.jar>
ENTRYPOINT ["java","-jar","spring-boot-docker-1.0.jar"]