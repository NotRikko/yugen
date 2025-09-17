package rikko.yugen.bootstrap;

import org.springframework.stereotype.Component;
import rikko.yugen.model.Artist;
import rikko.yugen.model.User;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArtistBootstrap {

    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;

    public ArtistBootstrap(ArtistRepository artistRepository, UserRepository userRepository) {
        this.artistRepository = artistRepository;
        this.userRepository = userRepository;
    }

    public void load() {
        if (artistRepository.count() == 0) {
            List<User> artistUsers = userRepository.findAll().stream()
                    .filter(User::getIsArtist)
                    .limit(3)
                    .collect(Collectors.toList());

            if (artistUsers.isEmpty()) {
                System.out.println("No artist users found. Cannot create artists.");
                return;
            }

            List<Artist> artists = List.of(
                    new Artist("DigitalDreamer", "I create colorful digital illustrations.", "artist1.png", artistUsers.get(0)),
                    new Artist("SketchWizard", "Sketches, characters, and concept art.", "artist2.png", artistUsers.get(1)),
                    new Artist("ColorStorm", "Abstract painter & digital artist.", "artist3.png", artistUsers.get(2))
            );

            artistRepository.saveAll(artists);
        }
    }
}