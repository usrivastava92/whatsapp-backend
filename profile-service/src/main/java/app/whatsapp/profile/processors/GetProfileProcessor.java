package app.whatsapp.profile.processors;

import app.whatsapp.profile.entities.User;
import app.whatsapp.profile.enums.EProfileServiceResponseCodes;
import app.whatsapp.commonweb.models.profile.response.ProfileResponse;
import app.whatsapp.profile.service.UserService;
import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.processors.IRequestProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetProfileProcessor implements IRequestProcessor<Long, Long, Optional<User>, ProfileResponse> {

    private UserService userService;

    public GetProfileProcessor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Long preProcess(Long profileId) {
        return profileId;
    }

    @Override
    public Optional<User> onProcess(Long profileId, Long profId) {
        return userService.findUserById(profId);
    }

    @Override
    public ProfileResponse postProcess(Long aLong, Long aLong2, Optional<User> user) {
        if (user.isPresent()) {
            return new ProfileResponse(ECommonResponseCodes.SUCCESS, user.get());
        }
        return new ProfileResponse(EProfileServiceResponseCodes.USER_NOT_FOUND);
    }
}
