package app.whatsapp.commonweb.models.profile.response;

import app.whatsapp.common.models.BaseResponse;
import app.whatsapp.common.models.IResponseCode;
import app.whatsapp.commonweb.models.profile.UserProfile;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddUserResponse extends BaseResponse {

    @JsonProperty("userProfile")
    private ProfileResponse profileResponse;

    public AddUserResponse(IResponseCode iResponseCode) {
        super(iResponseCode);
    }

    public AddUserResponse(IResponseCode iResponseCode, UserProfile userProfile) {
        super(iResponseCode);
        this.profileResponse = new ProfileResponse(userProfile);
    }

    public AddUserResponse(UserProfile userProfile) {
        this.profileResponse = new ProfileResponse(userProfile);
    }

}
