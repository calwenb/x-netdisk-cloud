FROM java:8

# 添加参数 pom.xm 中
ARG JAR_FILE

ADD target/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar","/app.jar"]