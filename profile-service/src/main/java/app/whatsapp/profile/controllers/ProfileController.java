package app.whatsapp.profile.controllers;

import app.whatsapp.common.models.IResponseCode;
import app.whatsapp.common.models.ResponseStatus;
import app.whatsapp.commonweb.annotations.log.Log;
import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.commonweb.models.profile.response.ConnectionsResponse;
import app.whatsapp.profile.entities.User;
import app.whatsapp.commonweb.models.profile.response.ProfileResponse;
import app.whatsapp.commonweb.models.profile.request.UpdateProfileRequest;
import app.whatsapp.commonweb.models.profile.response.UpdateProfileResponse;
import app.whatsapp.profile.processors.GetConnectionsProcessor;
import app.whatsapp.profile.processors.GetProfileProcessor;
import app.whatsapp.profile.processors.UpdateProfileProcessor;
import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.profile.utility.ModelMappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final GetProfileProcessor getProfileProcessor;
    private final UpdateProfileProcessor updateProfileProcessor;
    private final GetConnectionsProcessor getConnectionsProcessor;

    public ProfileController(GetProfileProcessor getProfileProcessor, UpdateProfileProcessor updateProfileProcessor, GetConnectionsProcessor getConnectionsProcessor) {
        this.getProfileProcessor = getProfileProcessor;
        this.updateProfileProcessor = updateProfileProcessor;
        this.getConnectionsProcessor = getConnectionsProcessor;
    }

    @Log
    @GetMapping("/get")
    public ProfileResponse getProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setUserProfile(ModelMappingUtils.getUserProfile(user));
        profileResponse.setResponseStatus(new ResponseStatus(ECommonResponseCodes.SUCCESS));
        return profileResponse;
    }

    @Log
    @GetMapping("/get/connections")
    public ConnectionsResponse getAll(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return getConnectionsProcessor.process(user);
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
