package app.whatsapp.gateway.service;

import app.whatsapp.commonweb.models.profile.response.ProfileResponse;

public interface ProfileService {

    ProfileResponse getProfile(String jwt);
    ProfileResponse getProfile(String jwt, String profileId);

}
