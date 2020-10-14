package app.whatsapp.commonweb.models.profile;


import app.whatsapp.commonweb.enums.profile.EProfileStatus;

public interface UserProfile {

    Long getId();

    String getUsername();

    String getPassword();

    String getFirstname();

    String getLastname();

    EProfileStatus getProfileStatus();

    String getEmail();

    String getContactNumber();

    void setUsername(String username);

    void setPassword(String password);

    void setFirstname(String firstname);

    void setLastname(String lastname);

    void setProfileStatus(EProfileStatus profileStatus);

    void setEmail(String email);

    void setContactNumber(String contactNumber);

}
