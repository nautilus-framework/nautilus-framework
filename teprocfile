language: java
install: mvn install
web: java $JAVA_OPTS -Dserver.port=$PORT -jar target/*.jar
jdk:
  - oraclejdk8
after_success:
  - bash <(curl -s https://codecov.io/bash)