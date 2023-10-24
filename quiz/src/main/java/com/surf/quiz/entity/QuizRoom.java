package com.surf.quiz.entity;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "quizroom")
public class QuizRoom {
    @Transient
    public static final String SEQUENCE_NAME = "quizroom_sequence";


    @Id
    private Long id;
    String roomName;
    String category;

    int roomMax;
    int roomCnt;

    int ready;
    int quizCnt;

    // false == 대기, true == 실행
    Boolean roomStatus=false;

    @CreatedDate
    private LocalDateTime createdDate;


    List<Member> users = new ArrayList<>();


    public void setId(Long id) {
        this.id = id;
    }
    public void setUsers(List<Member> users) {
        this.users = users;
    }
    public void memberExit(Member outMember) {
        users.removeIf(e -> e.getUserId().equals(outMember.getUserId()));
    }

    public void memberEntrance(Member inMember) {
        users.add(inMember);

        System.out.println("inMember = " + inMember);
        System.out.println("users = " + users);
    }

    public List<Member> readyMember(Member targetMember) {
        for (Member member : users) {
            if (member.getUserId().equals(targetMember.getUserId())) {
                member.setReady(true);
            }
        }
        return users;
    }
    public void unreadyMember(Member targetMember) {
        for (Member member : users) {
            if (member.getUserId().equals(targetMember.getUserId())) {
                member.setReady(false);
            }
        }
    }



}
