package app.whatsapp.commonweb.utils;

import app.whatsapp.common.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    public static Map<String, String> getQueryParam(String queryParamString) {
        if (StringUtils.isEmpty(queryParamString)) {
            return Collections.emptyMap();
        }
        Map<String, String> queryParams = new HashMap<>();
        for (String param : queryParamString.split(CommonConstants.SpecialChars.AMPERSAND)) {
            String[] pair = param.split(CommonConstants.SpecialChars.EQUALS);
            String key = pair[0];
            String value = CommonConstants.SpecialChars.BLANK;
            if (pair.length > 1) {
                value = pair[1];
            }
            queryParams.put(key, value);
        }
        return queryParams;
    }

}
