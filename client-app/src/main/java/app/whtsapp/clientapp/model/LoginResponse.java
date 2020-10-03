package app.whtsapp.clientapp.model;

import app.whatsapp.common.models.BaseResponse;
import lombok.Data;

@Data
public class LoginResponse extends BaseResponse {

    private String jwt;

}
