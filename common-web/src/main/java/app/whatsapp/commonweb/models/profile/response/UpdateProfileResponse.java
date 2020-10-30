package app.whatsapp.commonweb.models.profile.response;

import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.commonweb.enums.profile.EProfileStatus;
import app.whatsapp.common.models.BaseResponse;
import app.whatsapp.common.models.IResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateProfileResponse extends BaseResponse {

    private UserProfile userProfile;

}
