FROM wisvch/alpine-java:8_server-jre_unlimited 
ADD build/libs/payments.jar /srv/payments.jar
WORKDIR /srv
EXPOSE 8080
RUN mkdir config
CMD "/srv/payments.jar"
