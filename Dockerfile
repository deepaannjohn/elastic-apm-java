FROM openjdk:23

COPY target/*.jar app.jar
COPY elasticapm.properties /elasticapm.properties
COPY elastic-apm-agent-1.53.0.jar /elastic-apm-agent.jar

ENTRYPOINT [ \
  "java", \
  "-javaagent:/elastic-apm-agent.jar", \
  "-Delastic.apm.server_urls=https://my-observability-project-ec0716.apm.us-east-1.aws.elastic.cloud:443", \
  "-Delastic.apm.api_key=", \
  "-Delastic.apm.service_name=springboot-app", \
  "-Delastic.apm.application_packages=com.gcoo", \
  "-Delastic.apm.environment=dev", \
  "-Delastic.apm.config_file=/elasticapm.properties", \
  "-jar", \
  "/app.jar" \
]

EXPOSE 8080

