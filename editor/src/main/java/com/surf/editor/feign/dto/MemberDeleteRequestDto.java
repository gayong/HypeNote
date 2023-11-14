package com.surf.editor.feign.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Builder(toBuilder = true)
public class MemberDeleteRequestDto {
    private Map<Integer, String> rootDocument;
    private Map<Integer, Set<String>> sharedDocumentsList;
}
