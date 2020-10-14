package app.whatsapp.gateway.interceptor;

import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.commonweb.utils.HttpUtils;
import app.whatsapp.commonweb.models.profile.request.ValidateTokenRequest;
import app.whatsapp.commonweb.models.profile.response.ValidateTokenResponse;
import app.whatsapp.gateway.utils.MdcUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandshakeInterceptor.class);
    private static final String TOKEN_KEY = "token";
    private static final String PRINCIPAL = "principal";

    @Value("${application.profile.token.validate.url}")
    private String profileServiceValidateTokenUrl;

    private RestTemplate restTemplate;

    public WebSocketHandshakeInterceptor(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        Map<String, String> queryParams = HttpUtils.getQueryParam(serverHttpRequest.getURI().getQuery());
        String token = queryParams.get(TOKEN_KEY);
        if (StringUtils.isNotBlank(token)) {
            ValidateTokenRequest validateTokenRequest = new ValidateTokenRequest();
            validateTokenRequest.setAccessToken(token);
            ResponseEntity<ValidateTokenResponse> responseEntity = restTemplate.postForEntity(profileServiceValidateTokenUrl, validateTokenRequest, ValidateTokenResponse.class);
            if (responseEntity.getBody() != null && responseEntity.getBody().isValid()) {
                UserProfile userProfile = responseEntity.getBody().getUserProfile();
                map.put(PRINCIPAL, userProfile);
                MdcUtils.setUserDetailsInMdc(userProfile);
                return true;
            }
        }
        LOGGER.info("invalid authentication token : {} ", token);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
    }
}
