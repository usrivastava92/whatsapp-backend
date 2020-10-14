package app.whatsapp.profile.filters;

import app.whatsapp.profile.constants.ProfileServiceConstants;
import app.whatsapp.profile.service.UserService;
import app.whatsapp.commonweb.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtValidationFilter.class);

    private UserService userService;

    public JwtValidationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = extractJwt(httpServletRequest);
        JwtUtils.getSubject(jwt).ifPresent(username -> {
            JwtUtils.getClaim(jwt, ProfileServiceConstants.Extra.ID, Long.class).ifPresent(profileId -> {
                userService.findUserById(profileId).ifPresent(user -> {
                    UserDetails userDetails = user;
                    JwtUtils.Verifier.getInstance(jwt, user.getPassword()).withClaim(ProfileServiceConstants.Extra.ID, user.getId()).verify();
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                });
            });
        });
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String extractJwt(HttpServletRequest httpServletRequest) {
        String jwt = null;
        if (httpServletRequest.getCookies() != null) {
            Optional<Cookie> optionalCookie = Arrays.stream(httpServletRequest.getCookies())
                    .filter(i -> StringUtils.equals(ProfileServiceConstants.HttpConstants.COOKIE_KEY, i.getName())).findFirst();
            if (optionalCookie.isPresent()) {
                jwt = optionalCookie.get().getValue();
            }
        } else {
            Optional<String> optionalJwt = JwtUtils.getJwtFromHeader(httpServletRequest);
            if (optionalJwt.isPresent()) {
                jwt = optionalJwt.get();
            }
        }
        return jwt;
    }
}
