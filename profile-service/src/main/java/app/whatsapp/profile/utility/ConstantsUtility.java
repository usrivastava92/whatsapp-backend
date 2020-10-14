package app.whatsapp.profile.utility;

import app.whatsapp.common.constants.CommonConstants;

public class ConstantsUtility {

    public static String getUserCacheKey(Long id) {
        return new StringBuilder().append(CommonConstants.SpecialChars.UNDERSCORE).append(id).toString();
    }

    public static String getUserCacheKey(String id) {
        return new StringBuilder().append(CommonConstants.SpecialChars.UNDERSCORE).append(id).toString();
    }

}
