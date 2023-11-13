package com.surf.editor.feign.client;

import com.surf.editor.feign.dto.MemberListOpenFeignRequestDto;
import com.surf.editor.feign.dto.MemberListOpenFeignResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "MemberList",url = "https://www.hype-note.com/api/auth")
public interface MemberListOpenFeign {
    @PostMapping(value = "/user-info/pk-list")
    List<MemberListOpenFeignResponseDto> MemberList(MemberListOpenFeignRequestDto memberListOpenFeignRequestDto);

}
