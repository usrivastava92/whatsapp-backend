package app.whatsapp.commonweb.utils;

import app.whatsapp.common.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class HttpUtils {

    public static Map<String, String> getQueryParam(String queryParamString) {
        if (StringUtils.isEmpty(queryParamString)) {
            return Collections.emptyMap();
        }
        Map<String, String> queryParams = new HashMap<>();
        Arrays.stream(queryParamString.split(CommonConstants.SpecialChars.AMPERSAND)).forEach(param -> {
            String[] pair = param.split(CommonConstants.SpecialChars.EQUALS);
            String key = pair[0];
            String value = pair.length > 1 ? pair[1] : CommonConstants.SpecialChars.BLANK;
            queryParams.put(key, value);
        });
        return queryParams;
    }

    public static Optional<String> getJwtFromAuthorizationHeader(String authorizationHeader) {
        if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(CommonConstants.Http.BEARER)) {
            String jwt = authorizationHeader.replace(CommonConstants.Http.BEARER, CommonConstants.SpecialChars.BLANK).trim();
            if (StringUtils.isNotBlank(jwt)) {
                return Optional.of(jwt);
            }
        }
        return Optional.empty();
    }

}
