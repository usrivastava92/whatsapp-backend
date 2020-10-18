package app.whatsapp.gateway.interceptor;

import app.whatsapp.common.constants.CommonConstants;
import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.commonweb.utils.HttpUtils;
import app.whatsapp.commonweb.models.profile.request.ValidateTokenRequest;
import app.whatsapp.commonweb.models.profile.response.ValidateTokenResponse;
import app.whatsapp.gateway.constants.GatewayServiceConstants;
import app.whatsapp.gateway.utils.MdcUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandshakeInterceptor.class);

    @Value("${application.profile.token.validate.url}")
    private String profileServiceValidateTokenUrl;

    private RestTemplate restTemplate;

    public WebSocketHandshakeInterceptor(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        Map<String, String> queryParams = HttpUtils.getQueryParam(serverHttpRequest.getURI().getQuery());
        LOGGER.info("authenticating websocket connection : {} ", queryParams);
        String token = queryParams.get(GatewayServiceConstants.Extra.TOKEN);
        if (StringUtils.isNotBlank(token)) {
            ValidateTokenRequest validateTokenRequest = new ValidateTokenRequest();
            validateTokenRequest.setAccessToken(token);
            ResponseEntity<ValidateTokenResponse> responseEntity = restTemplate.postForEntity(profileServiceValidateTokenUrl, validateTokenRequest, ValidateTokenResponse.class);
            if (responseEntity.getBody() != null && responseEntity.getBody().isValid()) {
                UserProfile userProfile = responseEntity.getBody().getUserProfile();
                map.put(GatewayServiceConstants.Extra.PRINCIPAL, userProfile);
                MdcUtils.setUserDetailsInMdc(userProfile);
                return true;
            }
        }
        LOGGER.info("invalid authentication token : {} ", token);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        HttpServletResponse response = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, CommonConstants.SpecialChars.ASTERISK);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, CommonConstants.SpecialChars.ASTERISK);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, CommonConstants.SpecialChars.ASTERISK);
    }
}
