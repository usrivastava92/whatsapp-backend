package app.whatsapp.gateway.service;

import app.whatsapp.commonweb.models.profile.response.ProfileResponse;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Validated
public interface ProfileService {

    ProfileResponse getProfile(@Valid @NotEmpty String jwt);

    ProfileResponse getProfile(@Valid @NotEmpty String jwt, @Valid @NotEmpty String profileId);

}
