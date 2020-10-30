package app.whatsapp.profile.processors;

import app.whatsapp.profile.entities.User;
import app.whatsapp.profile.enums.EProfileServiceResponseCodes;
import app.whatsapp.commonweb.models.profile.request.ValidateTokenRequest;
import app.whatsapp.commonweb.models.profile.response.ValidateTokenResponse;
import app.whatsapp.commonweb.models.profile.response.ProfileResponse;
import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.models.ResponseStatus;
import app.whatsapp.common.processors.IRequestProcessor;
import app.whatsapp.commonweb.services.CacheService;
import app.whatsapp.profile.utility.ModelMappingUtils;
import org.springframework.stereotype.Component;

import static app.whatsapp.profile.constants.ProfileServiceConstants.*;

import java.util.Optional;

@Component
public class ValidateTokenProcessor implements IRequestProcessor<ValidateTokenRequest, ValidateTokenRequest, Optional<User>, ValidateTokenResponse> {

    private final CacheService cacheService;

    public ValidateTokenProcessor(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public ValidateTokenRequest preProcess(ValidateTokenRequest request) {
        return request;
    }

    @Override
    public Optional<User> onProcess(ValidateTokenRequest request, ValidateTokenRequest serviceRequest) {
        String accessToken = request.getAccessToken();
        Optional<User> user = cacheService.get(accessToken, User.class);
        cacheService.del(accessToken);
        return user;
    }

    @Override
    public ValidateTokenResponse postProcess(ValidateTokenRequest request, ValidateTokenRequest serviceRequest, Optional<User> user) {
        ValidateTokenResponse validateTokenResponse = new ValidateTokenResponse();
        if (user.isPresent()) {
            validateTokenResponse.setUserProfile(ModelMappingUtils.getUserProfile(user.get()));
            validateTokenResponse.setValid(true);
            validateTokenResponse.setResponseStatus(new ResponseStatus(ECommonResponseCodes.SUCCESS));
        }
        validateTokenResponse.setResponseStatus(new ResponseStatus(EProfileServiceResponseCodes.INVALID_TOKEN));
        return validateTokenResponse;
    }
}
