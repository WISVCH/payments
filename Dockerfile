FROM wisvch/spring-boot-base:1
COPY ./build/libs/payments.jar /srv/payments.jar
CMD ["/srv/payments.jar"]
