#환경
FROM amazoncorretto:17
#변수설정
ARG JAR_FILE=build/libs/*.jar
#복사
COPY ${JAR_FILE} discovery.jar
#Port번호
EXPOSE 8761
#서버 명령어
ENTRYPOINT ["java", "-jar", "/discovery.jar"]

#Testing Line
#인식 -> Not yet