package app.whatsapp.gateway.service.impl;

import app.whatsapp.common.constants.CommonConstants;
import app.whatsapp.common.models.ResponseStatus;
import app.whatsapp.commonweb.models.profile.response.ProfileResponse;
import app.whatsapp.gateway.service.ProfileService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Value("${application.profile.get.url}")
    private String getProfileUrl;

    private final RestTemplate restTemplate;

    public ProfileServiceImpl(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ProfileResponse getProfile(String jwt) {
        return getProfileResponse(jwt, getProfileUrl);
    }

    @Override
    public ProfileResponse getProfile(String jwt, String profileId) {
        return getProfileResponse(jwt, getProfileUrl + CommonConstants.SpecialChars.FORWARD_SLASH + profileId);
    }

    private ProfileResponse getProfileResponse(String jwt, String uri) {
        HttpHeaders headers = new HttpHeaders();
        String authorizationHeader = CommonConstants.Http.BEARER + CommonConstants.SpecialChars.WHITE_SPACE + jwt;
        headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            return restTemplate.exchange(uri, HttpMethod.GET, entity, ProfileResponse.class).getBody();
        } catch (Exception e) {
            throw new RuntimeException("failed to fetch profile ", e);
        }
    }
}
