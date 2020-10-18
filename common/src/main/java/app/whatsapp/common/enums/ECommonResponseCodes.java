package app.whatsapp.common.enums;

import app.whatsapp.common.models.IResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ECommonResponseCodes implements IResponseCode {

    SYSTEM_ERROR("500", "F", "Some error occurred"),
    INVALID_REQUEST("400", "F", "Invalid request"),
    INVALID_PARAM("60", "F", "Invalid parameter"),
    SUCCESS("200", "S", "Success");

    private String code;
    private String status;
    private String message;
}
