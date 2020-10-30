package app.whatsapp.profile.utility;

import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.profile.entities.User;

public class ModelMappingUtils {

    private ModelMappingUtils() {

    }

    public static UserProfile getUserProfile(User user) {
        return new UserProfile(user.getId(), user.getUsername(),
                user.getFirstname(), user.getLastname(),
                user.getEmail(), user.getContactNumber(),
                user.getProfileStatus());
    }

}
