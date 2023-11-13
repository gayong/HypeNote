package com.surf.diagram.diagram.fegin;



import com.surf.diagram.diagram.dto.editor.ApiResponse;
import com.surf.diagram.diagram.dto.editor.EditorListRequestDto;
import com.surf.diagram.diagram.dto.editor.EditorListResponseDto;
import com.surf.diagram.diagram.dto.member.UserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name="server-auth", url="https://k9e101.p.ssafy.io")
public interface MemberServiceFeignClient {
    // pk 보내서 user 정보 받기
    @GetMapping("/api/auth/user-info/userpk/{userPk}")
    UserInfoResponseDto userInfoByUserPk(@PathVariable int userPk);

    @GetMapping("/api/auth/user-info")
    UserInfoResponseDto userInfoByToken(@RequestHeader("Authorization") String token);
}
