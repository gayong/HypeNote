package com.surf.diagram.diagram.dto.response;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter
public class ClassificationResult {
    private List<Category> categories;
    private String languageCode;
    private boolean languageSupported;


    @Getter
    @Setter
    public static class Category {
        private String name;
        private float confidence;
    }

}
