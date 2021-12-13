FROM wisvch/spring-boot-base:2.5.5
COPY ./build/libs/payments.jar /srv/payments.jar
CMD ["/srv/payments.jar"]
