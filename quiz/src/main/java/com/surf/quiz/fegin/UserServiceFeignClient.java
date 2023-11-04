package com.surf.quiz.fegin;


import com.surf.quiz.dto.editor.ApiResponse;
import com.surf.quiz.dto.editor.EditorCheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


//@FeignClient(name="server-member", url="https://k9e101.p.ssafy.io")
//public interface UserServiceFeignClient {
//    @GetMapping("/api/member/{userId}")
//    ApiResponse<EditorCheckResponse> getEditor(@PathVariable String userId);
//}
