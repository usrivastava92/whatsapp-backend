package app.whatsapp.gateway.models;

import app.whatsapp.commonweb.models.profile.response.ProfileResponse;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@Data
@RequiredArgsConstructor
public class StompPrincipal implements Principal {

    @NonNull
    private final ProfileResponse profile;

    @Override
    public String getName() {
        return String.valueOf(profile.getId());
    }

}
