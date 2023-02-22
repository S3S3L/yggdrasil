FROM openjdk:11
WORKDIR /opt/zookeeper
COPY apache-zookeeper-3.8.1-bin.tar.gz .
# ADD  https://dlcdn.apache.org/zookeeper/zookeeper-3.8.1/apache-zookeeper-3.8.1-bin.tar.gz .
RUN tar -xvf apache-zookeeper-3.8.1-bin.tar.gz
RUN mv /opt/zookeeper/apache-zookeeper-3.8.1-bin /opt/zookeeper/zookeeper
RUN mv /opt/zookeeper/zookeeper/conf/zoo_sample.cfg /opt/zookeeper/zookeeper/conf/zoo.cfg
CMD ["/opt/zookeeper/zookeeper/bin/zkServer.sh", "start-foreground"]