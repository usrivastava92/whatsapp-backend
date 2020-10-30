package app.whatsapp.profile.processors;

import app.whatsapp.common.models.ResponseStatus;
import app.whatsapp.commonweb.models.profile.response.CreateTokenResponse;
import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.processors.IRequestProcessor;
import app.whatsapp.commonweb.services.CacheService;
import app.whatsapp.profile.entities.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static app.whatsapp.profile.constants.ProfileServiceConstants.*;

@Component
public class CreateTokenProcessor implements IRequestProcessor<User, User, String, CreateTokenResponse> {

    private final CacheService cacheService;

    @Value("${application.access-token.expiry.seconds:60}")
    private long accessTokenExpiry;

    public CreateTokenProcessor(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public User preProcess(User request) {
        return request;
    }

    @Override
    public String onProcess(User user, User serviceRequest) {
        String accessToken = RandomStringUtils.randomAlphanumeric(4) +
                System.currentTimeMillis() +
                RandomStringUtils.randomAlphanumeric(4);
        cacheService.set(accessToken, user, Duration.ofSeconds(accessTokenExpiry));
        return accessToken;
    }

    @Override
    public CreateTokenResponse postProcess(User request, User serviceRequest, String accessToken) {
        CreateTokenResponse createTokenResponse = new CreateTokenResponse(accessToken);
        createTokenResponse.setResponseStatus(new ResponseStatus(ECommonResponseCodes.SUCCESS));
        return createTokenResponse;
    }

}
