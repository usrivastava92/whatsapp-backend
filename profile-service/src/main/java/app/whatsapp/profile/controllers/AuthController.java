package app.whatsapp.profile.controllers;

import app.whatsapp.commonweb.annotations.log.Log;
import app.whatsapp.commonweb.models.profile.request.LoginRequest;
import app.whatsapp.commonweb.models.profile.response.LoginResponse;
import app.whatsapp.commonweb.models.profile.request.AddUserRequest;
import app.whatsapp.commonweb.models.profile.response.AddUserResponse;
import app.whatsapp.profile.processors.LoginProcessor;
import app.whatsapp.profile.processors.UserRegistrationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private LoginProcessor loginProcessor;
    @Autowired
    private UserRegistrationProcessor userRegistrationProcessor;

    @Log
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        LoginResponse responsePayload = loginProcessor.process(request);
        return responsePayload;
    }

    @Log
    @PostMapping("/register")
    public AddUserResponse register(@RequestBody AddUserRequest request) {
        AddUserResponse responsePayload = userRegistrationProcessor.process(request);
        return responsePayload;
    }

}
