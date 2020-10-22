package app.whatsapp.profile.processors;

import app.whatsapp.profile.constants.ProfileServiceConstants;
import app.whatsapp.profile.entities.User;
import app.whatsapp.profile.enums.EProfileServiceResponseCodes;
import app.whatsapp.commonweb.models.profile.request.LoginRequest;
import app.whatsapp.commonweb.models.profile.response.LoginResponse;
import app.whatsapp.commonweb.models.profile.response.ProfileResponse;
import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.processors.IRequestProcessor;
import app.whatsapp.commonweb.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class LoginProcessor implements IRequestProcessor<LoginRequest, LoginRequest, LoginResponse, LoginResponse> {

    @Value("${application.jwt.expiry.seconds:900}")
    private long jwtExpiry;

    private AuthenticationManager authenticationManager;

    public LoginProcessor(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public LoginRequest preProcess(LoginRequest request) {
        return request;
    }

    @Override
    public LoginResponse onProcess(LoginRequest request, LoginRequest serviceRequest) {
        LoginResponse loginResponse;
        try {
            String username = serviceRequest.getUsername();
            String password = serviceRequest.getPassword();
            Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = (User) authentication.getPrincipal();
            String jwt = JwtUtils.Builder.getInstance().withSubject(username)
                    .withClaim(ProfileServiceConstants.Extra.ID, user.getId())
                    .signWith(password).withValidity(Duration.ofSeconds(this.jwtExpiry)).build();
            loginResponse = new LoginResponse(ECommonResponseCodes.SUCCESS, jwt, new ProfileResponse(user));
        } catch (BadCredentialsException e) {
            loginResponse = new LoginResponse(EProfileServiceResponseCodes.INVALID_CREDENTIALS);
        } catch (Exception e) {
            log.error("Exception in generating jwt {} ", e);
            loginResponse = new LoginResponse(ECommonResponseCodes.SYSTEM_ERROR);
        }
        return loginResponse;
    }

    @Override
    public LoginResponse postProcess(LoginRequest request, LoginRequest serviceRequest, LoginResponse serviceResponse) {
        return serviceResponse;
    }
}
