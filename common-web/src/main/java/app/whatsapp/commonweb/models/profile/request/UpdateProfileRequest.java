package app.whatsapp.commonweb.models.profile.request;

import app.whatsapp.commonweb.enums.profile.EProfileStatus;
import app.whatsapp.commonweb.models.profile.UserProfile;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateProfileRequest implements UserProfile {

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

    public UpdateProfileRequest(UserProfile userProfile) {
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
