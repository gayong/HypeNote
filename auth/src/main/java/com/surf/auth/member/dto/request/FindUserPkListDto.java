package com.surf.auth.member.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindUserPkListDto {

    private List<Integer> userPkList;
}
