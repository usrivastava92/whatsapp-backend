package app.whatsapp.profile.controllers;

import app.whatsapp.commonweb.annotations.log.Log;
import app.whatsapp.profile.entities.User;
import app.whatsapp.commonweb.models.profile.response.ProfileResponse;
import app.whatsapp.commonweb.models.profile.request.UpdateProfileRequest;
import app.whatsapp.commonweb.models.profile.response.UpdateProfileResponse;
import app.whatsapp.profile.processors.GetProfileProcessor;
import app.whatsapp.profile.processors.UpdateProfileProcessor;
import app.whatsapp.common.enums.ECommonResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private GetProfileProcessor getProfileProcessor;
    @Autowired
    private UpdateProfileProcessor updateProfileProcessor;

    @Log
    @GetMapping("/get")
    public ProfileResponse getProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ProfileResponse(ECommonResponseCodes.SUCCESS, user);
    }

    @Log
    @GetMapping("/get/{id}")
    public ProfileResponse getProfile(@PathVariable Long id) {
        return getProfileProcessor.process(id);
    }

    @Log
    @PostMapping("/update")
    public UpdateProfileResponse updateProfile(@RequestBody UpdateProfileRequest request) {
        return updateProfileProcessor.process(request);
    }

}
