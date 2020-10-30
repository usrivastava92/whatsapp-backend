package app.whatsapp.sessions.controllers;

import app.whatsapp.commonweb.annotations.log.Log;
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

    private final AddSessionProcessor addSessionProcessor;
    private final EvictSessionProcessor evictSessionProcessor;

    public SessionController(AddSessionProcessor addSessionProcessor, EvictSessionProcessor evictSessionProcessor) {
        this.addSessionProcessor = addSessionProcessor;
        this.evictSessionProcessor = evictSessionProcessor;
    }

    @Log
    @PostMapping("/add")
    public AddSessionResponse addSession(@RequestBody AddSessionRequest addSessionRequest){
        return addSessionProcessor.process(addSessionRequest);
    }

    @Log
    @PostMapping("/evict")
    public EvictSessionResponse evictSession(@RequestBody EvictSessionRequest evictSessionRequest){
        return evictSessionProcessor.process(evictSessionRequest);
    }
}
