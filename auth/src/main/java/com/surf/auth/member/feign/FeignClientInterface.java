package com.surf.auth.member.feign;

import com.surf.auth.member.dto.request.RootsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="chat", url="https://api.openai.com/v1/")

public interface FeignClientInterface {

    @DeleteMapping("/api/editor/root-delete")
    HttpStatus rootDelete(@RequestBody RootsDto rootsDto);
}
