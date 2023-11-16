package com.surf.auth.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnshareRequestDto {

    private List<Integer> userPkList;
    private String documentId;
}
