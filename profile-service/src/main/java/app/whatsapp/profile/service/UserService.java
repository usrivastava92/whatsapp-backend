package app.whatsapp.profile.service;

import app.whatsapp.profile.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService {

    UserDetails loadUserByUsername(String var1) throws UsernameNotFoundException;
    Optional<User> findUserById(Long id);
    User update(User user);
    User addUser(User user);

}
