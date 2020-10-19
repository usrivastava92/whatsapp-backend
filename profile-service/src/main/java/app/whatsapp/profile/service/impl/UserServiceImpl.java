package app.whatsapp.profile.service.impl;

import app.whatsapp.profile.entities.User;
import app.whatsapp.profile.jpa.repositories.UserRepository;
import app.whatsapp.profile.service.UserService;
import app.whatsapp.commonweb.services.CacheService;
import app.whatsapp.profile.utility.ConstantsUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static app.whatsapp.profile.constants.ProfileServiceConstants.*;

import java.time.Duration;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private CacheService cacheService;

    @Value("${application.cache.user.expiry.seconds:900}")
    private long userCacheExpiry;

    public UserServiceImpl(UserRepository userRepository, CacheService cacheService) {
        this.userRepository = userRepository;
        this.cacheService = cacheService;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        Optional<User> userFromDb = userRepository.findByUsername(username);
        if (userFromDb.isPresent()) {
            user = userFromDb.get();
            cacheService.set(ConstantsUtility.getUserCacheKey(user.getId()), user, Duration.ofSeconds(userCacheExpiry));
            return user;
        }
        throw new UsernameNotFoundException(Extra.USER_NOT_FOUND);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        User user = null;
        Optional<User> userFromCache = cacheService.get(ConstantsUtility.getUserCacheKey(id), User.class);
        if (userFromCache.isPresent()) {
            user = userFromCache.get();
        } else {
            Optional<User> userFromDb = userRepository.findById(id);
            if (userFromDb.isPresent()) {
                cacheService.set(ConstantsUtility.getUserCacheKey(id), userFromDb.get(), Duration.ofSeconds(userCacheExpiry));
                user = userFromDb.get();
            }
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User update(User user) {
        User updatedUser = userRepository.save(user);
        cacheService.set(ConstantsUtility.getUserCacheKey(updatedUser.getId()), updatedUser, Duration.ofSeconds(userCacheExpiry));
        return updatedUser;
    }

    @Override
    public User addUser(User user) {
        User addedUser = userRepository.save(user);
        cacheService.set(ConstantsUtility.getUserCacheKey(addedUser.getId()), addedUser, Duration.ofSeconds(userCacheExpiry));
        return user;
    }

}
