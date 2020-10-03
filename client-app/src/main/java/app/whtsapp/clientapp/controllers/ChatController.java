package app.whtsapp.clientapp.controllers;

import app.whtsapp.clientapp.model.LoginRequest;
import app.whtsapp.clientapp.processors.StartChattingProcessor;
import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.models.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private StartChattingProcessor startChattingProcessor;

    @PostMapping("/start")
    public BaseResponse startChatting(@RequestBody LoginRequest request) {
        startChattingProcessor.process(request);
        return new BaseResponse(ECommonResponseCodes.SUCCESS);
    }

}
