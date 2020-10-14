package app.whtsapp.clientapp.model;

import app.whatsapp.common.models.BaseResponse;
import lombok.Data;

@Data
public class CreateTokenResponse extends BaseResponse {

    private String accessToken;

}
