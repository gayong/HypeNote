package com.surf.quiz.dto.member;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class FindUserPkListDto {

    private List<Integer> userPkList;

    public FindUserPkListDto(List<Integer> pkList) {
        this.userPkList = pkList;
    }
}
