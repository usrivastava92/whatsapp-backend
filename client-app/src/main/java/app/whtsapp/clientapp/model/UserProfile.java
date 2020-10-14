package app.whtsapp.clientapp.model;

import lombok.Data;

@Data
public class UserProfile {

    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String contactNumber;
    private String password;

}
