package com.cw.cwu.controller.admin;

import com.cw.cwu.dto.AnswerDTO;
import com.cw.cwu.dto.QnADTO;
import com.cw.cwu.service.admin.AdminAnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminAnsController {

    private final AdminAnsService adminAnsService;

    // 답변 작성
    @PostMapping("ans/write")
    public String writeAns(@RequestBody AnswerDTO dto){
        String userId = dto.getUserId();
        Integer answerId = adminAnsService.writeAns(dto, userId);
        return "result : " + answerId;
    }

    // 답변 삭제
    @DeleteMapping("ans/delete/{questionId}")
    public Map<String, String>clearAns(@PathVariable(name = "questionId") Integer questionId){
        adminAnsService.deleteAns(questionId);
        return Map.of("삭제 수행 결과","성공");
    }
}
