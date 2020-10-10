package app.whatsapp.profile.filters;

import app.whatsapp.profile.constants.ProfileServiceConstants;
import app.whatsapp.profile.service.UserService;
import app.whatsapp.commonweb.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        JwtUtil.getJwtFromHeader(httpServletRequest).ifPresent(jwt -> {
            JwtUtil.getSubject(jwt).ifPresent(username -> {
                JwtUtil.getClaim(jwt, ProfileServiceConstants.Extra.ID, Long.class).ifPresent(profileId -> {
                    userService.findUserById(profileId).ifPresent(user -> {
                        UserDetails userDetails = user;
                        JwtUtil.Verifier.getInstance(jwt, user.getPassword()).withClaim(ProfileServiceConstants.Extra.ID, user.getId()).verify();
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    });
                });
            });
        });
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
