FROM java:8 
VOLUME /tmp
ADD ./target/ebuy-reporting-api-1.0.1.jar  ebuy-reporting-api.jar
RUN sh -c 'touch /ebuy-reporting-api.jar'
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java  -jar  /ebuy-reporting-api.jar" ]
