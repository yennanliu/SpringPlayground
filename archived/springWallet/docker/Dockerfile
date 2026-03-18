# book 5.81
# https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/docker/Dockerfile
FROM java:8
VOLUME /tmp
RUN mkdir /app
ADD wallet-1.0-SNAPSHOT.jar /app/wallet.jar
ADD runboot.sh /app/
RUN bash -c 'touch /app/wallet.jar'
WORKDIR /app
RUN chmod a+x runboot.sh
EXPOSE 9090
CMD /app/runboot.sh