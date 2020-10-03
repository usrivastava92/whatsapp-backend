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
public class UpdateProfileResponse extends BaseResponse implements UserProfile {

    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String contactNumber;
    private EProfileStatus profileStatus;

    public UpdateProfileResponse(IResponseCode iResponseCode) {
        super(iResponseCode);
    }

    public UpdateProfileResponse(IResponseCode iResponseCode, UserProfile userProfile) {
        super(iResponseCode);
        this.id = userProfile.getId();
        this.username = userProfile.getUsername();
        this.password = userProfile.getPassword();
        this.firstname = userProfile.getFirstname();
        this.lastname = userProfile.getLastname();
        this.email = userProfile.getEmail();
        this.contactNumber = userProfile.getContactNumber();
        this.profileStatus = userProfile.getProfileStatus();
    }

    public UpdateProfileResponse(UserProfile userProfile) {
        this.id = userProfile.getId();
        this.username = userProfile.getUsername();
        this.password = userProfile.getPassword();
        this.firstname = userProfile.getFirstname();
        this.lastname = userProfile.getLastname();
        this.email = userProfile.getEmail();
        this.contactNumber = userProfile.getContactNumber();
        this.profileStatus = userProfile.getProfileStatus();
    }

}
