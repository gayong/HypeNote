package com.surf.quiz.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Getter
@Setter
public class SearchMemberDto {
    String roomName;
    int quizCnt;
    boolean single;
    List<Integer> pages;
    List<Integer> sharePages;

    @Getter
    @Setter
    @Document(collection = "user")
    public static class Member {

        private Long userPk;
        private String userName;

        // True == 방장, False == 일반
        private boolean host;

        // True == 레디, False == 대기
        private boolean ready;
    }
}
