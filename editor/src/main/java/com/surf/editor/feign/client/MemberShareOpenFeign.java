package com.surf.editor.feign.client;

import com.surf.editor.feign.dto.MemberShareRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "MemberShareClient",url = "https://www.hype-note.com/api/auth")
public interface MemberShareOpenFeign {
    @PutMapping(value = "/share") // 수정 필요
    void MemberShare(MemberShareRequestDto memberShareRequestDto);
}
