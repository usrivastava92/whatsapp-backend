package app.whatsapp.sessions.controllers;

import app.whatsapp.commonweb.models.sessions.request.AddSessionRequest;
import app.whatsapp.commonweb.models.sessions.response.AddSessionResponse;
import app.whatsapp.sessions.processors.AddSessionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private AddSessionProcessor addSessionProcessor;

    @PostMapping("/add")
    public AddSessionResponse addSession(@RequestBody AddSessionRequest addSessionRequest){
        return addSessionProcessor.process(addSessionRequest);
    }

}
