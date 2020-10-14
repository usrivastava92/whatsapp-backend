package app.whatsapp.profile.config;

import app.whatsapp.profile.enums.EProfileServiceResponseCodes;
import app.whatsapp.profile.exceptions.JwtValidationException;
import app.whatsapp.common.models.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = {JwtValidationException.class})
    public ResponseEntity<BaseResponse> handleJwtValidationException() {
        return new ResponseEntity<>(new BaseResponse(EProfileServiceResponseCodes.INVALID_JWT), HttpStatus.BAD_REQUEST);
    }

}