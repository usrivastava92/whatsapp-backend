package app.whatsapp.profile.interceptors;

import app.whatsapp.profile.constants.ProfileServiceConstants;
import app.whatsapp.commonweb.utils.HttpUtils;
import app.whatsapp.commonweb.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class BaseInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        setMDC(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        clearMDC();
    }

    private void clearMDC() {
        MDC.clear();
    }

    private void setMDC(HttpServletRequest request) {
        Map<String, String> queryParams = HttpUtils.getQueryParam(request.getQueryString());
        String requestId = queryParams.get(ProfileServiceConstants.Extra.REQUEST_ID);
        if (StringUtils.isNotBlank(requestId)) {
            MDC.put(ProfileServiceConstants.Extra.REQUEST_ID, requestId);
        }
        JwtUtils.getJwtFromHeader(request).ifPresent(jwt -> {
            JwtUtils.getSubject(jwt).ifPresent(username -> {
                MDC.put(ProfileServiceConstants.Extra.USERNAME, username);
            });
        });
    }

}
