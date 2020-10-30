package app.whatsapp.commonweb.models.profile.request;

import app.whatsapp.commonweb.enums.profile.EProfileStatus;
import app.whatsapp.commonweb.models.profile.UserProfile;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateProfileRequest {

    @NotNull
    private Long id;
    @NotNull
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String contactNumber;
    private EProfileStatus profileStatus;

}
