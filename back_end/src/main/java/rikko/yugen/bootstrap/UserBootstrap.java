package rikko.yugen.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
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
                    new User("alice", "Alice Smith", "alice@example.com", passwordEncoder.encode("password1"), "alice.png", false),
                    new User("bob", "Bob Johnson", "bob@example.com", passwordEncoder.encode("password2"), "bob.png", true),
                    new User("charlie", "Charlie Rose", "charlie@example.com", passwordEncoder.encode("password3"), "charlie.png", false),
                    new User("diana", "Diana Prince", "diana@example.com", passwordEncoder.encode("password4"), "diana.png", true),
                    new User("edward", "Edward Nigma", "edward@example.com", passwordEncoder.encode("password5"), "edward.png", true)
            );

            userRepository.saveAll(users);
        }
    }
}
