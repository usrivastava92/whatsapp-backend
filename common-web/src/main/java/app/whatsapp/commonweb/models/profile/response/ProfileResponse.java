package app.whatsapp.commonweb.models.profile.response;

import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.common.models.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponse extends BaseResponse {

    private UserProfile userProfile;

}
