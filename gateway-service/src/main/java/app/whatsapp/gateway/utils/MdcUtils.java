package app.whatsapp.gateway.utils;

import app.whatsapp.commonweb.models.profile.UserProfile;
import org.slf4j.MDC;

public class MdcUtils {

    public static void setUserDetailsInMdc(UserProfile userProfile) {
        MDC.put("userId", String.valueOf(userProfile.getId()));
    }

    public static void clear() {
        MDC.clear();
    }


}
