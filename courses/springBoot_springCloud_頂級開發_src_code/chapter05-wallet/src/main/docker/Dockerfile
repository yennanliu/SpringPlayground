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
