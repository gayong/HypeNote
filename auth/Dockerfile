#환경
FROM amazoncorretto:17
#변수설정
ARG JAR_FILE=build/libs/*.jar
#복사
COPY ${JAR_FILE} auth.jar
#Port번호
EXPOSE 8082
#서버 명령어
ENTRYPOINT ["java", "-jar", "/auth.jar"]

#Testing Line
#인식 -> Not yet