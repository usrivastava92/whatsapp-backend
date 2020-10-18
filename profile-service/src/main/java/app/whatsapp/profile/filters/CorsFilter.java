package app.whatsapp.profile.filters;

import app.whatsapp.common.constants.CommonConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter extends org.springframework.web.filter.CorsFilter {

    public CorsFilter(CorsConfigurationSource configSource) {
        super(configSource);
    }

    private static final String ALLOW_HEADERS = "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Access-Control-Allow-Methods, Authorization ,Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Set-Cookie";
    private static final String ALLOW_METHODS = "GET, POST, PATCH, PUT, DELETE, OPTIONS";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, CommonConstants.SpecialChars.ASTERISK);
        //response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, Boolean.TRUE.toString()); // not allowed with allow origin = *
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOW_METHODS);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, ALLOW_HEADERS);
        super.doFilterInternal(request, response, filterChain);
    }
}
