package com.surf.quiz.fegin;


import com.surf.quiz.common.BaseResponse;
import com.surf.quiz.dto.diagram.DiagramResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name="server-diagram", url="https://k9e101.p.ssafy.io")
@FeignClient(name="server-diagram", url="http://localhost:8087")
public interface DiagramServiceFeignClient {
    @GetMapping("/api/diagram/{userId}")
    BaseResponse<DiagramResponseDto> getNodes(@PathVariable int userId);
}
