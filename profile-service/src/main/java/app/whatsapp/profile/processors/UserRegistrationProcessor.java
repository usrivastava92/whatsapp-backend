package app.whatsapp.profile.processors;

import app.whatsapp.common.models.ResponseStatus;
import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.profile.entities.User;
import app.whatsapp.commonweb.enums.profile.EProfileStatus;
import app.whatsapp.commonweb.models.profile.request.AddUserRequest;
import app.whatsapp.commonweb.models.profile.response.AddUserResponse;
import app.whatsapp.profile.service.UserService;
import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.processors.IRequestProcessor;
import app.whatsapp.profile.utility.ModelMappingUtils;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationProcessor implements IRequestProcessor<AddUserRequest, User, UserProfile, AddUserResponse> {

    private final UserService userService;

    public UserRegistrationProcessor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User preProcess(AddUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setContactNumber(request.getContactNumber());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setProfileStatus(EProfileStatus.ACTIVE);
        return user;
    }

    @Override
    public UserProfile onProcess(AddUserRequest request, User serviceRequest) {
        return ModelMappingUtils.getUserProfile(userService.addUser(serviceRequest));
    }

    @Override
    public AddUserResponse postProcess(AddUserRequest request, User serviceRequest, UserProfile serviceResponse) {
        AddUserResponse addUserResponse = new AddUserResponse();
        addUserResponse.setResponseStatus(new ResponseStatus(ECommonResponseCodes.SUCCESS));
        addUserResponse.setUserProfile(serviceResponse);
        return addUserResponse;
    }
}
