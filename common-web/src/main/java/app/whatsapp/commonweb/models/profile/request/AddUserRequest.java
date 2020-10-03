package app.whatsapp.commonweb.models.profile.request;

import app.whatsapp.commonweb.enums.profile.EProfileStatus;
import app.whatsapp.commonweb.models.profile.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequest implements UserProfile {

    @JsonIgnore
    private Long id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String firstname;
    private String lastname;
    @NotNull
    private String contactNumber;
    private String email;
    @JsonIgnore
    private EProfileStatus profileStatus;

}
