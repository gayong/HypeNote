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
    private String content;

    private int roomMax;
    private int roomCnt;

    private int readyCnt;
    private int quizCnt;

    // false == 대기, true == 실행
    private boolean roomStatus;


    private String createdDate;

    private List<String> pages;
    private List<String> sharePages;

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


    public boolean memberReady(Long userId, String action) {
        boolean isProcessed = false;

        if (action.equals("ready")) {
            for (MemberDto member : users) {
                if (member.getUserPk().equals(userId)) {
                    if (member.isReady()) {
                        break; // 이미 레디인 경우 반복문 종료
                    }
                    member.setReady(true);
                    isProcessed = true;
                    break;
                }
            }
        } else if (action.equals("unready")) {
            for (MemberDto member : users) {
                if (member.getUserPk().equals(userId)) {
                    if (!member.isReady()) {
                        break; // 이미 언레디인 경우 반복문 종료
                    }
                    member.setReady(false);
                    isProcessed = true;
                    break;
                }
            }
        }

        return isProcessed;
    }

}
