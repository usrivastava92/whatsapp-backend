package app.whatsapp.sessions.controllers;

import app.whatsapp.commonweb.models.sessions.request.AddSessionRequest;
import app.whatsapp.commonweb.models.sessions.request.EvictSessionRequest;
import app.whatsapp.commonweb.models.sessions.response.AddSessionResponse;
import app.whatsapp.commonweb.models.sessions.response.EvictSessionResponse;
import app.whatsapp.sessions.processors.AddSessionProcessor;
import app.whatsapp.sessions.processors.EvictSessionProcessor;
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

    @Autowired
    private EvictSessionProcessor evictSessionProcessor;

    @PostMapping("/add")
    public AddSessionResponse addSession(@RequestBody AddSessionRequest addSessionRequest){
        return addSessionProcessor.process(addSessionRequest);
    }

    @PostMapping("/evict")
    public EvictSessionResponse evictSession(@RequestBody EvictSessionRequest evictSessionRequest){
        return evictSessionProcessor.process(evictSessionRequest);
    }
}
