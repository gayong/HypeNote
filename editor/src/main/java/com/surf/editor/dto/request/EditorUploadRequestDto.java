package com.surf.editor.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class EditorUploadRequestDto {
    private MultipartFile multipartFile;
}
