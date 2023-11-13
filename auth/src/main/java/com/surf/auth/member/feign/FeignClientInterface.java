package com.surf.auth.member.feign;

import com.surf.auth.member.dto.ApiResponse;
import com.surf.auth.member.dto.request.RootsDto;
import com.surf.auth.member.dto.request.SharedDocumentsRootListDto;
import com.surf.auth.member.dto.response.FeignClientUserListDto;
import com.surf.auth.member.dto.response.UserPkResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="chat", url="https://www.hype-note.com")
public interface FeignClientInterface {

    @DeleteMapping("/api/editor/root-delete")
    HttpStatus rootDelete(@RequestBody RootsDto rootsDto);

    @PostMapping("/api/editor/root-owner")
    ApiResponse<FeignClientUserListDto> rootInspect(@RequestBody SharedDocumentsRootListDto sharedDocumentsRootListDto);
}
