package app.whatsapp.commonweb.models.profile.response;

import app.whatsapp.commonweb.enums.profile.EProfileStatus;
import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.common.models.BaseResponse;
import app.whatsapp.common.models.IResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponse extends BaseResponse implements UserProfile {

    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String contactNumber;
    private EProfileStatus profileStatus;
    @JsonIgnore
    private String password;

    public ProfileResponse(IResponseCode iResponseCode) {
        super(iResponseCode);
    }

    public ProfileResponse(IResponseCode iResponseCode, UserProfile userProfile) {
        super(iResponseCode);
        this.id = userProfile.getId();
        this.username = userProfile.getUsername();
        this.firstname = userProfile.getFirstname();
        this.lastname = userProfile.getLastname();
        this.email = userProfile.getEmail();
        this.contactNumber = userProfile.getContactNumber();
        this.profileStatus = userProfile.getProfileStatus();
    }

    public ProfileResponse(UserProfile userProfile) {
        this.id = userProfile.getId();
        this.username = userProfile.getUsername();
        this.firstname = userProfile.getFirstname();
        this.lastname = userProfile.getLastname();
        this.email = userProfile.getEmail();
        this.contactNumber = userProfile.getContactNumber();
        this.profileStatus = userProfile.getProfileStatus();
    }
}
