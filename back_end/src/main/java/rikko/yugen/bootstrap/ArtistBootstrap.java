package rikko.yugen.bootstrap;

import org.springframework.stereotype.Component;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Image;
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
                    .collect(Collectors.toList());

            if (artistUsers.isEmpty()) {
                System.out.println("No artist users found. Cannot create artists.");
                return;
            }

            List<Artist> artists = List.of(
                    createArtist(
                            "Sandrone",
                            "I create colorful digital illustrations.",
                            "https://i.pinimg.com/736x/18/c2/f7/18c2f7a303ad5b05d8a41c6b7e4c062b.jpg",
                            "https://preview.redd.it/sandrone-being-playable-v0-ic6fiimnuzjf1.jpeg?auto=webp&s=0b1b20d08edb2fabe5809ce07ec97ea9be8329fa",
                            artistUsers.get(0)
                    ),
                    createArtist(
                            "Colombina",
                            "Sketches, characters, and concept art.",
                            "https://preview.redd.it/how-excited-are-you-for-columbina-v0-fxmoi5kd6f0d1.png?width=640&crop=smart&auto=webp&s=cf18e46f714251fd1bba42a9ef9fbd8b647a4249",
                            "https://preview.redd.it/sandrone-being-playable-v0-ic6fiimnuzjf1.jpeg?auto=webp&s=0b1b20d08edb2fabe5809ce07ec97ea9be8329fa",
                            artistUsers.get(1)
                    ),
                    createArtist(
                            "Signora",
                            "Abstract painter & digital artist.",
                            "https://m.media-amazon.com/images/I/612SzJjmqGL.jpg",
                            "https://preview.redd.it/sandrone-being-playable-v0-ic6fiimnuzjf1.jpeg?auto=webp&s=0b1b20d08edb2fabe5809ce07ec97ea9be8329fa",
                            artistUsers.get(2)
                    ),
                    createArtist(
                            "Rikko",
                            "Test Rikko.",
                            "https://m.media-amazon.com/images/I/612SzJjmqGL.jpg",
                            "https://preview.redd.it/sandrone-being-playable-v0-ic6fiimnuzjf1.jpeg?auto=webp&s=0b1b20d08edb2fabe5809ce07ec97ea9be8329fa",
                            artistUsers.get(3)
                    )
            );

            artistRepository.saveAll(artists);
        }
    }

    private Artist createArtist(String name, String bio, String profileUrl, String bannerUrl, User user) {
        Artist artist = new Artist();
        artist.setArtistName(name);
        artist.setBio(bio);
        artist.setUser(user);

        if (profileUrl != null) {
            Image profileImage = new Image();
            profileImage.setUrl(profileUrl);
            profileImage.setProfileForArtist(artist);
            artist.setProfileImage(profileImage);
        }

        if (bannerUrl != null) {
            Image bannerImage = new Image();
            bannerImage.setUrl(bannerUrl);
            bannerImage.setBannerForArtist(artist);
            artist.setBannerImage(bannerImage);
        }

        return artist;
    }
}