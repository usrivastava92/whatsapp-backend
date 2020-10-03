package app.whatsapp.commonweb.models.profile.response;

import app.whatsapp.common.models.BaseResponse;
import app.whatsapp.common.models.IResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse extends BaseResponse {

    private String jwt;
    @JsonProperty("userProfile")
    private ProfileResponse userProfile;

    public LoginResponse(IResponseCode responseCode){
        super(responseCode);
    }

    public LoginResponse(IResponseCode responseCode, String accessToken, ProfileResponse userProfile){
        super(responseCode);
        this.jwt = accessToken;
        this.userProfile = userProfile;
    }

}
