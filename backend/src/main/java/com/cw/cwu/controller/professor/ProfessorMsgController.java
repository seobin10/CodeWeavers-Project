package com.cw.cwu.controller.professor;


import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RequestMapping("/api/professor/msg")
@RestController
public class ProfessorMsgController {

    final DefaultMessageService messageService;

    public ProfessorMsgController(@Value("${coolSms.api.apikey}") String apiKey, @Value("${coolSms.api.apikey2}") String apiKey2){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiKey2, "https://api.coolsms.co.kr");
    }

    @Value("${coolSms.phone.num}")
    private String fromPhoneNumber;

    @PostMapping("/send")
    public SingleMessageSentResponse sendOne(String to, String text) {
        try {
            Message msg = new Message();
            msg.setFrom(fromPhoneNumber);  // 이 부분 변경
            msg.setTo(to);
            msg.setText(text);
            return this.messageService.sendOne(new SingleMessageSendingRequest(msg));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("메시지 전송 중 오류가 발생했습니다.", e);
        }
    }
}
