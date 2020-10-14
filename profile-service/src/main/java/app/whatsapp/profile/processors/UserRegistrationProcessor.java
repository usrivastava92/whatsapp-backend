package app.whatsapp.profile.processors;

import app.whatsapp.profile.entities.User;
import app.whatsapp.commonweb.enums.profile.EProfileStatus;
import app.whatsapp.commonweb.models.profile.request.AddUserRequest;
import app.whatsapp.commonweb.models.profile.response.AddUserResponse;
import app.whatsapp.profile.service.UserService;
import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.processors.IRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationProcessor implements IRequestProcessor<AddUserRequest, User, User, AddUserResponse> {

    @Autowired
    private UserService userService;

    @Override
    public User preProcess(AddUserRequest request) {
        User user = new User(request);
        user.setProfileStatus(EProfileStatus.ACTIVE);
        return user;
    }

    @Override
    public User onProcess(AddUserRequest request, User serviceRequest) {
        return userService.addUser(serviceRequest);
    }

    @Override
    public AddUserResponse postProcess(AddUserRequest request, User serviceRequest, User serviceResponse) {
        return new AddUserResponse(ECommonResponseCodes.SUCCESS, serviceResponse);
    }
}
