#환경
FROM amazoncorretto:17
#변수설정
ARG JAR_FILE=build/libs/*.jar
#복사
COPY ${JAR_FILE} config.jar
#Port번호
EXPOSE 8888
#서버 명령어
ENTRYPOINT ["java", "-jar", "/config.jar"]

#Testing Line
#인식 -> Not yet