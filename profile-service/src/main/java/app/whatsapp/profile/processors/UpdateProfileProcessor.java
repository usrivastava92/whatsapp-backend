package app.whatsapp.profile.processors;

import app.whatsapp.common.models.ResponseStatus;
import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.profile.entities.User;
import app.whatsapp.commonweb.models.profile.request.UpdateProfileRequest;
import app.whatsapp.commonweb.models.profile.response.UpdateProfileResponse;
import app.whatsapp.profile.service.UserService;
import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.processors.IRequestProcessor;
import app.whatsapp.profile.utility.ModelMappingUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UpdateProfileProcessor implements IRequestProcessor<UpdateProfileRequest, User, UserProfile, UpdateProfileResponse> {

    private final UserService userService;

    public UpdateProfileProcessor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User preProcess(UpdateProfileRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            loggedInUser.setFirstname(request.getFirstname());
            loggedInUser.setLastname(request.getLastname());
            loggedInUser.setContactNumber(request.getContactNumber());
            loggedInUser.setEmail(request.getEmail());
            return loggedInUser;
        }
        throw new RuntimeException("Security context should not be null for modifying user");
    }

    @Override
    public UserProfile onProcess(UpdateProfileRequest request, User serviceRequest) {
        User updatedUser = userService.update(serviceRequest);
        return ModelMappingUtils.getUserProfile(updatedUser);
    }

    @Override
    public UpdateProfileResponse postProcess(UpdateProfileRequest userProfilePayload, User serviceRequest, UserProfile serviceResponse) {
        UpdateProfileResponse updateProfileResponse = new UpdateProfileResponse();
        updateProfileResponse.setUserProfile(serviceResponse);
        updateProfileResponse.setResponseStatus(new ResponseStatus(ECommonResponseCodes.SUCCESS));
        return updateProfileResponse;
    }
}
