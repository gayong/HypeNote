package com.surf.editor.feign.client;

import com.surf.editor.feign.dto.DiagramEditorSaveRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "diagramClient",url = "https://k9e101.p.ssafy.io/api/diagram")
public interface DiagramOpenFeign {

    @PostMapping(value = "/editor")
    void DiagramEditorSave(DiagramEditorSaveRequestDto diagramEditorSaveRequestDto);
}
