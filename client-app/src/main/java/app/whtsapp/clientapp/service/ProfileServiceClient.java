package app.whtsapp.clientapp.service;

import app.whtsapp.clientapp.model.LoginRequest;
import app.whatsapp.common.constants.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProfileServiceClient {

    private RestTemplate restTemplate;

    public ProfileServiceClient(@Qualifier("restTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String HOST = "localhost";
    private static final String PORT = "8080";
    private static final String LOGIN_URI = "http://" + HOST + ":" + PORT + "/auth/login";
    private static final String CREATE_TOKEN_URI = "http://" + HOST + ":" + PORT + "/token/create";
    private static final String VALIDATE_TOKEN_URI = "http://" + HOST + ":" + PORT + "/token/validate";

    public ResponseEntity<String> login(LoginRequest request) {
        return restTemplate.postForEntity(LOGIN_URI, request, String.class);
    }

    public ResponseEntity<String> createToken(String jwt) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        HttpEntity<String> entity = new HttpEntity<>(CommonConstants.SpecialChars.BLANK, httpHeaders);
        return restTemplate.postForEntity(LOGIN_URI, entity, String.class);
    }

}
