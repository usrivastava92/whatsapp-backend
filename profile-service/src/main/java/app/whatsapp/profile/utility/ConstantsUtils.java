package app.whatsapp.profile.utility;

import app.whatsapp.common.constants.CommonConstants;

public class ConstantsUtils {

    private ConstantsUtils(){

    }

    public static String getUserCacheKey(Long id) {
        return CommonConstants.SpecialChars.UNDERSCORE + id;
    }

}
