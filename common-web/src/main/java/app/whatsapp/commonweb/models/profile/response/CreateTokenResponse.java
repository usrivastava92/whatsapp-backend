package app.whatsapp.commonweb.models.profile.response;

import app.whatsapp.common.models.BaseResponse;
import app.whatsapp.common.models.IResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateTokenResponse extends BaseResponse {

    private String accessToken;

    public CreateTokenResponse(IResponseCode responseCode) {
        super(responseCode);
    }

    public CreateTokenResponse(IResponseCode iResponseCode, String accessToken) {
        super(iResponseCode);
        this.accessToken = accessToken;
    }
}
