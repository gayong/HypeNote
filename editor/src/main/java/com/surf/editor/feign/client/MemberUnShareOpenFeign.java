package com.surf.editor.feign.client;

import com.surf.editor.feign.dto.MemberShareRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "MemberUnShareClient",url = "https://www.hype-note.com/api/auth")
public interface MemberUnShareOpenFeign {
    @DeleteMapping (value = "/unshare") // 수정 필요
    void MemberUnShare(MemberShareRequestDto memberShareRequestDto);
}
