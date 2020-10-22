package app.whatsapp.gateway.filters;

import app.whatsapp.common.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getCookies() != null && request.getCookies().length != 0 && StringUtils.isNotBlank(request.getHeader(HttpHeaders.ORIGIN))) {
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, Boolean.TRUE.toString());
        } else {
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, CommonConstants.SpecialChars.ASTERISK);
        }
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, CommonConstants.SpecialChars.ASTERISK);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, CommonConstants.SpecialChars.ASTERISK);
        super.doFilterInternal(request, response, filterChain);
    }
}
