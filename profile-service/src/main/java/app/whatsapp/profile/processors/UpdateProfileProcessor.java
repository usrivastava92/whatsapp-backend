package app.whatsapp.profile.processors;

import app.whatsapp.profile.entities.User;
import app.whatsapp.commonweb.models.profile.request.UpdateProfileRequest;
import app.whatsapp.commonweb.models.profile.response.UpdateProfileResponse;
import app.whatsapp.profile.service.UserService;
import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.processors.IRequestProcessor;
import org.springframework.stereotype.Component;

@Component
public class UpdateProfileProcessor implements IRequestProcessor<UpdateProfileRequest, User, User, UpdateProfileResponse> {

    private UserService userService;

    public UpdateProfileProcessor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User preProcess(UpdateProfileRequest request) {
        return new User(request);
    }

    @Override
    public User onProcess(UpdateProfileRequest request, User serviceRequest) {
        return userService.update(serviceRequest);
    }

    @Override
    public UpdateProfileResponse postProcess(UpdateProfileRequest userProfilePayload, User serviceRequest, User serviceResponse) {
        return new UpdateProfileResponse(ECommonResponseCodes.SUCCESS, serviceResponse);
    }
}
