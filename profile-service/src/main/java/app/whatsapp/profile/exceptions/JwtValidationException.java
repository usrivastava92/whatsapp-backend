package app.whatsapp.profile.exceptions;

import app.whatsapp.profile.enums.EProfileServiceResponseCodes;

public class JwtValidationException extends BaseException {

    public JwtValidationException() {
        super(EProfileServiceResponseCodes.INVALID_JWT);
    }

}
