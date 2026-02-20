package rikko.yugen.bootstrap;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import rikko.yugen.model.Image;
import rikko.yugen.model.Role;
import rikko.yugen.model.User;
import rikko.yugen.repository.UserRepository;

import java.util.List;

@Component
public class UserBootstrap {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserBootstrap(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void load() {
        if (userRepository.count() == 0) {
            List<User> users = List.of(
                    createUser("alice", "Alice Smith", "alice@example.com", "password1", "alice.png", false),
                    createUser("bob", "Bob Johnson", "bob@example.com", "password2", "bob.png", true),
                    createUser("charlie", "Charlie Rose", "charlie@example.com", "password3", "charlie.png", false),
                    createUser("diana", "Diana Prince", "diana@example.com", "password4", "diana.png", true),
                    createUser("edward", "Edward Nigma", "edward@example.com", "password5", "edward.png", true),
                    createUser("Test", "Rikko", "holymoly@email.com", "asdfasdf",
                            "https://pbs.twimg.com/profile_images/1966106142481731584/0U9vHmdl_400x400.jpg", true)
            );

            userRepository.saveAll(users);
        }
    }

    private User createUser(String username, String displayName, String email, String rawPassword, String profileImageUrl, boolean isArtist) {
        User user = new User();
        user.setUsername(username);
        user.setDisplayName(displayName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setIsArtist(isArtist);
        user.setRole(Role.USER);

        if (profileImageUrl != null) {
            Image profileImage = new Image();
            profileImage.setUrl(profileImageUrl);
            profileImage.setUser(user);
            user.setProfileImage(profileImage);
        }

        return user;
    }
}
