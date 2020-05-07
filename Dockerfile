FROM maven:3.6-jdk-11-slim
ADD . /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean install
CMD ["java","-jar","/usr/src/app/target/operator-value-diff-jar-with-dependencies.jar"]