package app.whatsapp.commonweb.models.profile.response;

import app.whatsapp.common.models.BaseResponse;
import app.whatsapp.commonweb.models.profile.UserProfile;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ValidateTokenResponse extends BaseResponse {

    private boolean isValid;
    private UserProfile userProfile;

}
