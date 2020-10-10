package app.whatsapp.profile.processors;

import app.whatsapp.commonweb.models.profile.response.CreateTokenResponse;
import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.processors.IRequestProcessor;
import app.whatsapp.commonweb.services.CacheService;
import app.whatsapp.profile.entities.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import static app.whatsapp.profile.constants.ProfileServiceConstants.*;

@Component
public class CreateTokenProcessor implements IRequestProcessor<User, User, String, CreateTokenResponse> {

    private CacheService cacheService;

    public CreateTokenProcessor(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public User preProcess(User request) {
        return request;
    }

    @Override
    public String onProcess(User request, User serviceRequest) {
        String accessToken = new StringBuilder()
                .append(RandomStringUtils.randomAlphanumeric(4))
                .append(System.currentTimeMillis())
                .append(RandomStringUtils.randomAlphanumeric(4))
                .toString();
        cacheService.hSet(CacheConstants.ACCESS_TOKEN_USER_CACHE_KEY, accessToken, request);
        return accessToken;
    }

    @Override
    public CreateTokenResponse postProcess(User request, User serviceRequest, String accessToken) {
        return new CreateTokenResponse(ECommonResponseCodes.SUCCESS, accessToken);
    }

}
