package app.whatsapp.profile.controllers;

import app.whatsapp.commonweb.annotations.log.Log;
import app.whatsapp.profile.entities.User;
import app.whatsapp.commonweb.models.profile.response.CreateTokenResponse;
import app.whatsapp.commonweb.models.profile.request.ValidateTokenRequest;
import app.whatsapp.commonweb.models.profile.response.ValidateTokenResponse;
import app.whatsapp.profile.processors.CreateTokenProcessor;
import app.whatsapp.profile.processors.ValidateTokenProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private CreateTokenProcessor createTokenProcessor;
    @Autowired
    private ValidateTokenProcessor validateTokenProcessor;

    @Log
    @PostMapping("/create")
    public CreateTokenResponse createToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return createTokenProcessor.process(user);
    }

    @Log
    @PostMapping("/validate")
    public ValidateTokenResponse validateToken(@RequestBody ValidateTokenRequest request) {
        return validateTokenProcessor.process(request);
    }

}
