package com.surf.quiz.entity;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Document(collection = "quizroom")
public class QuizRoom {
    @Transient
    public static final String SEQUENCE_NAME = "quizroom_sequence";


    @Id
    private Long id;
    private String roomName;
    private String category;

    private int roomMax;
    private int roomCnt;

    private int readyCnt;
    private int quizCnt;

    // false == 대기, true == 실행
    private boolean roomStatus;

    @CreatedDate
    private LocalDateTime createdDate;

    private List<Integer> pages;
    private List<Integer> sharePages;

    private boolean single;

    private List<Long> inviteUsers;
    private List<Member> users = new ArrayList<>();


    public void setId(Long id) {
        this.id = id;
    }

    public void memberIn(Member inMember) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        users.add(inMember);
    }
    public void memberOut(Member outMember) {
        users.removeIf(e -> e.getUserId().equals(outMember.getUserId()));
    }


    public void memberReady(Long userId) {
        for (Member member : users) {
            if (member.getUserId().equals(userId)) {
                member.setReady(true);
            }
        }
    }
    public void memberUnready(Long userId) {
        for (Member member : users) {
            if (member.getUserId().equals(userId)) {
                member.setReady(false);
            }
        }
    }



}
