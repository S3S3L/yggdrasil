FROM openjdk:11
WORKDIR /opt/zookeeper
COPY zookeeper.tar.gz .
RUN tar -xvf zookeeper.tar.gz
RUN mkdir zookeeper
RUN mv /opt/zookeeper/apache-zookeeper-*/* /opt/zookeeper/zookeeper/
RUN mv /opt/zookeeper/zookeeper/conf/zoo_sample.cfg /opt/zookeeper/zookeeper/conf/zoo.cfg
CMD ["/opt/zookeeper/zookeeper/bin/zkServer.sh", "start-foreground"]