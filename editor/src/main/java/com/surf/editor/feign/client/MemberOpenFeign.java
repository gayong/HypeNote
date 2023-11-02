package com.surf.editor.feign.client;

import com.surf.editor.feign.dto.MemberEditorSaveRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "MemberClient",url = "https://k9e101.p.ssafy.io/api/member")
public interface MemberOpenFeign {
    @PutMapping(value = "/documentAdd")
    void MemberEditorSave(MemberEditorSaveRequestDto memberEditorSaveRequestDto);

}
