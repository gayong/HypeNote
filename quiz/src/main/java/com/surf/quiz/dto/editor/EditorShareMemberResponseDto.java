package com.surf.quiz.dto.editor;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class EditorShareMemberResponseDto {
    private List<Integer> userList;
}
