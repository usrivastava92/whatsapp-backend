package app.whatsapp.gateway.interceptor;

import app.whatsapp.common.constants.CommonConstants;
import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.commonweb.models.profile.response.ProfileResponse;
import app.whatsapp.commonweb.models.sessions.response.AddSessionResponse;
import app.whatsapp.commonweb.utils.HttpUtils;
import app.whatsapp.gateway.models.StompPrincipal;
import app.whatsapp.gateway.service.ProfileService;
import app.whatsapp.gateway.service.SessionRegistryService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class ChannelInterceptor implements org.springframework.messaging.support.ChannelInterceptor {

    private final SessionRegistryService sessionRegistryService;
    private final ProfileService profileService;

    public ChannelInterceptor(SessionRegistryService sessionRegistryService, ProfileService profileService) {
        this.sessionRegistryService = sessionRegistryService;
        this.profileService = profileService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            authenticate(accessor);
        }
        if (Objects.nonNull(accessor.getUser())) {
            return message;
        }
        throw new RuntimeException("Invalid credentials!!");
    }

    private void authenticate(StompHeaderAccessor accessor) {
        if (accessor.getUser() == null && accessor.getNativeHeader(HttpHeaders.AUTHORIZATION) != null) {
            List<String> authTokens = accessor.getNativeHeader(HttpHeaders.AUTHORIZATION);
            assert authTokens != null;
            String authorizationHeader = authTokens.get(0).trim();
            log.info("Authorization Header : {} ", authorizationHeader);
            HttpUtils.getJwtFromAuthorizationHeader(authorizationHeader).ifPresent(jwt -> {
                ProfileResponse profileResponse = profileService.getProfile(jwt);
                new UserProfile();
                if (profileResponse.getResponseStatus().getStatus().equals(CommonConstants.Alphabets.S)) {
                    StompPrincipal stompPrincipal = new StompPrincipal(profileResponse.getUserProfile());
                    MDC.put(CommonConstants.Extra.USER_ID, String.valueOf(profileResponse.getUserProfile().getId()));
                    AddSessionResponse addSessionResponse = sessionRegistryService.registerSession(stompPrincipal);
                    if (!addSessionResponse.getResponseStatus().getStatus().equals(CommonConstants.Alphabets.S)) {
                        throw new RuntimeException("failed to register session");
                    }
                    log.info("user authenticated : {}", profileResponse);
                    accessor.setUser(stompPrincipal);
                }
            });
        }
    }
}
