package app.whatsapp.gateway.models;

import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.commonweb.models.profile.response.ProfileResponse;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@Data
@RequiredArgsConstructor
public class StompPrincipal implements Principal {

    @NonNull
    private final UserProfile userProfile;

    @Override
    public String getName() {
        return String.valueOf(userProfile.getId());
    }

}
