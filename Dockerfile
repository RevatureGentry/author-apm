FROM maven:3.6.3-jdk-11-slim as builder
WORKDIR /app
COPY . .
RUN mvn clean install
RUN cp target/author-service-0.0.1-SNAPSHOT.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM adoptopenjdk:11-jre-hotspot
COPY --from=builder /app/src/main/resources/elastic-apm-agent-1.18.1.jar /apm/elastic-agent.jar
ENV JAVA_MEM_OPTS="-Xms512m -Xmx512m"
ENV JAVA_OPTS="${JAVA_OPTS} -javaagent:/apm/elastic-agent.jar"
ENV JAVA_OPTS="${JAVA_OPTS} -Delastic.apm.service_name=author-ms"
ENV JAVA_OPTS="${JAVA_OPTS} -Delastic.apm.application_packages=com.revature"
ENV JAVA_OPTS="${JAVA_OPTS} -Delastic.apm.server_urls=http://elk-apm-server:8200"
ENV JAVA_OPTS="${JAVA_OPTS} -XX:+UnlockExperimentalVMOptions -XX:+UseZGC"
ENV JAVA_OPTS="${JAVA_OPTS} ${JAVA_MEM_OPTS}"
ENV JAVA_OPTS="${JAVA_OPTS} -Dspring.profiles.active=firebase,apm,kubernetes"
WORKDIR /app
ARG DEPENDENCY=/app
COPY --from=builder $DEPENDENCY/dependencies/ ./
COPY --from=builder $DEPENDENCY/snapshot-dependencies/ ./
COPY --from=builder $DEPENDENCY/spring-boot-loader/ ./
COPY --from=builder $DEPENDENCY/application/ ./
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom org.springframework.boot.loader.JarLauncher"]
