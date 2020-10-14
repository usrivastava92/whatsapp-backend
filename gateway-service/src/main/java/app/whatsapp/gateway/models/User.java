package app.whatsapp.gateway.models;
import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.commonweb.models.profile.response.ProfileResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@NoArgsConstructor
public class User extends ProfileResponse implements Principal {

    @Override
    public String getName() {
        return getUsername();
    }


    public User(UserProfile userProfile) {
        super(userProfile);
    }

    @Override
    public String toString() {
        return "User" + super.toString().replace("ProfileResponse","");
    }
}
