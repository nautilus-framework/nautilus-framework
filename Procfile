language: java
install: mvn compile
web: java -jar nautilus-web/target/nautilus-web-1.0.0.jar
jdk:
  - oraclejdk8
after_success:
  - bash <(curl -s https://codecov.io/bash)