package com.surf.auth.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class DocumentDeleteDto {

    private Map<Integer, String> rootDocument;
    private Map<Integer, Set<String>> sharedDocumentsList;
}