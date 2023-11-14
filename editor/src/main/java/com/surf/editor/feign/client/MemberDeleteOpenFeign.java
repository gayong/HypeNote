package com.surf.editor.feign.client;

import com.surf.editor.feign.dto.MemberDeleteRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;

@FeignClient(name = "MemberDelete",url = "https://www.hype-note.com/api/auth")
public interface MemberDeleteOpenFeign {

    @DeleteMapping(value = "/document-delete")
    void memberDelete(MemberDeleteRequestDto memberDeleteRequestDto);

}
