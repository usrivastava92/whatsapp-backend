package app.whtsapp.clientapp.model;

import app.whatsapp.common.models.BaseResponse;
import lombok.Data;

@Data
public class ValidateTokenResponse extends BaseResponse {

    private boolean isValid;

}
