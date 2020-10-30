package app.whatsapp.commonweb.config;

import app.whatsapp.common.constants.CommonConstants;
import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.models.BaseResponse;
import app.whatsapp.common.models.IResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<BaseResponse> exception() {
        return getResponse(null, ECommonResponseCodes.SYSTEM_ERROR);
    }

    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity<BaseResponse> handleBindException(BindException ex) {
        return getResponseEntity(ex.getBindingResult());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<BaseResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return getResponseEntity(ex.getBindingResult());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<BaseResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Set<String> errors = new HashSet<>();
        for (ConstraintViolation constraintViolation : ex.getConstraintViolations()) {
            StringBuilder sb = new StringBuilder();
            Iterator<Path.Node> iterator = constraintViolation.getPropertyPath().iterator();
            while (iterator.hasNext()) {
                Path.Node node = iterator.next();
                if (!iterator.hasNext()) {
                    sb.append(node)
                            .append(CommonConstants.SpecialChars.WHITE_SPACE)
                            .append(CommonConstants.SpecialChars.COLON)
                            .append(CommonConstants.SpecialChars.WHITE_SPACE)
                            .append(constraintViolation.getMessage());
                }
            }
            errors.add(sb.toString());
        }
        return getResponse(errors.toString(), ECommonResponseCodes.INVALID_REQUEST);
    }

    private ResponseEntity<BaseResponse> getResponseEntity(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return getResponse(errors.toString(), ECommonResponseCodes.INVALID_REQUEST);
    }

    private ResponseEntity<BaseResponse> getResponse(String message, IResponseCode responseCode) {
        BaseResponse baseResponseBody = new BaseResponse(responseCode);
        if (StringUtils.isNotBlank(message)) {
            baseResponseBody.getResponseStatus().setMessage(message);
        }
        return ResponseEntity.ok(baseResponseBody);
    }
}
