package app.whatsapp.profile.exceptions;

import app.whatsapp.common.models.IResponseCode;
import lombok.Getter;

@Getter
public class BaseException extends Exception {

    private IResponseCode responseCode;

    public BaseException(IResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

}
