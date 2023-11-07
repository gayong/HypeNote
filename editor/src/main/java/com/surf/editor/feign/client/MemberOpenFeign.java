package com.surf.editor.feign.client;

import com.surf.editor.feign.dto.MemberEditorSaveRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "MemberClient",url = "https://www.hype-note.com/api/auth")
public interface MemberOpenFeign {
    @PutMapping(value = "/root-save")
    void MemberEditorSave(MemberEditorSaveRequestDto memberEditorSaveRequestDto);

}
