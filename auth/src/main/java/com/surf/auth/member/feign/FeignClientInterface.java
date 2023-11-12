package com.surf.auth.member.feign;

import com.surf.auth.member.dto.request.RootsDto;
import com.surf.auth.member.dto.request.SharedDocumentsListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="chat", url="https://www.hype-note.com")
public interface FeignClientInterface {

    @DeleteMapping("/api/editor/root-delete")
    HttpStatus rootDelete(@RequestBody RootsDto rootsDto);

    @PostMapping("/api/editor/root-owner")
    List<Integer> rootInspect(@RequestBody SharedDocumentsListDto sharedDocumentsListDto);
}
