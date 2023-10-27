package com.surf.quiz.entity;


import com.surf.quiz.dto.MemberDto;
import com.surf.quiz.dto.UserDto;
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

    private List<UserDto> inviteUsers;
    private List<MemberDto> users;


    public void setId(Long id) {
        this.id = id;
    }

    public void memberIn(MemberDto inMember) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        users.add(inMember);
    }
    public void memberOut(MemberDto outMember) {
        users.removeIf(e -> e.getUserPk().equals(outMember.getUserPk()));
    }


    public void memberReady(Long userId, String action) {
        if (action.equals("ready")) {
            for (MemberDto member : users) {
                if (member.getUserPk().equals(userId)) {
                    member.setReady(true);
                }
            }
        } else if (action.equals("unready")) {
            for (MemberDto member : users) {
                if (member.getUserPk().equals(userId)) {
                    member.setReady(false);
                }
            }
        }
    }

}
