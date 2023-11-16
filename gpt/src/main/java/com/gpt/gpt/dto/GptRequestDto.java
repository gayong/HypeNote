package com.gpt.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GptRequestDto {
    private List<Map<String, String>> messages;
    private String model;
//    private boolean stream;
}
