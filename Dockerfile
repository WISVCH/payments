FROM wisvch/alpine-java:8_server-jre_unlimited 
ADD build/libs/payments.jar /srv/payments.jar
WORKDIR /srv
EXPOSE 8080
RUN mkdir config
ENTRYPOINT ["java","-Dspring.profiles.active=container","-jar","/srv/payments.jar"]
