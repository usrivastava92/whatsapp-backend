package app.whatsapp.profile.entities;

import app.whatsapp.commonweb.enums.profile.EProfileStatus;
import app.whatsapp.commonweb.models.profile.UserProfile;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails, UserProfile {

    private static final long serialVersionUID = -5564974736195263335L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true)
    private Long id;
    @Column(updatable = false, unique = true)
    private String username;
    private String password;
    @Column(nullable = false)
    private String firstname;
    private String lastname;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String contactNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EProfileStatus profileStatus;

    public User(UserProfile userProfile) {
        this.id = userProfile.getId();
        this.username = userProfile.getUsername();
        this.password = userProfile.getPassword();
        this.firstname = userProfile.getFirstname();
        this.lastname = userProfile.getLastname();
        this.contactNumber = userProfile.getContactNumber();
        this.email = userProfile.getEmail();
        this.profileStatus = userProfile.getProfileStatus();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.EMPTY_SET;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
