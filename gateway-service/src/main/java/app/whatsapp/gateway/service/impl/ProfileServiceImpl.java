package app.whatsapp.gateway.service.impl;

import app.whatsapp.common.constants.CommonConstants;
import app.whatsapp.commonweb.models.profile.response.ProfileResponse;
import app.whatsapp.gateway.service.ProfileService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Service
@Validated
public class ProfileServiceImpl implements ProfileService {

    @Value("${application.profile.get.url}")
    private String getProfileUrl;

    private RestTemplate restTemplate;

    public ProfileServiceImpl(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ProfileResponse getProfile(@Valid @NotEmpty String jwt) {
        return getProfileResponse(jwt, getProfileUrl);
    }

    @Override
    public ProfileResponse getProfile(@Valid @NotEmpty String jwt,@Valid @NotEmpty String profileId) {
        return getProfileResponse(jwt, new StringBuilder(getProfileUrl).append(CommonConstants.SpecialChars.FORWARD_SLASH).append(profileId).toString());
    }

    private ProfileResponse getProfileResponse(String jwt, String uri) {
        HttpHeaders headers = new HttpHeaders();
        String authorizationHeader = new StringBuilder(CommonConstants.Http.BEARER).append(CommonConstants.SpecialChars.WHITE_SPACE).append(jwt).toString();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + authorizationHeader);
        HttpEntity entity = new HttpEntity<>(headers);
        try {
            return restTemplate.exchange(uri, HttpMethod.GET, entity, ProfileResponse.class).getBody();
        } catch (Exception e) {
            throw new RuntimeException("failed to fetch profile");
        }
    }
}
