package com.surf.diagram.diagram.fegin;



import com.surf.diagram.diagram.dto.editor.ApiResponse;
import com.surf.diagram.diagram.dto.editor.EditorCheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name="server-editor", url="https://k9e101.p.ssafy.io")
public interface EditorServiceFeignClient {
    @GetMapping("/api/editor/{editorId}")
    ApiResponse<EditorCheckResponse> getEditor(@PathVariable String editorId);
}
