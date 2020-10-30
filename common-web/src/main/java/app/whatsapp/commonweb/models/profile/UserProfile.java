package app.whatsapp.commonweb.models.profile;

import app.whatsapp.commonweb.enums.profile.EProfileStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String contactNumber;
    private EProfileStatus profileStatus;

}
