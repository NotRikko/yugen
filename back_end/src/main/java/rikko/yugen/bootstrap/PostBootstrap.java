package rikko.yugen.bootstrap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Post;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PostBootstrap {

    private final PostRepository postRepository;
    private final ArtistRepository artistRepository;

    public PostBootstrap(PostRepository postRepository, ArtistRepository artistRepository) {
        this.postRepository = postRepository;
        this.artistRepository = artistRepository;
    }

    public void load() {
        if (postRepository.count() == 0) {
            List<Artist> artists = artistRepository.findAll();

            if (artists.isEmpty()) {
                System.out.println("No artists found. Cannot create posts.");
                return;
            }

            List<Post> posts = List.of(
                    new Post("Check out my new artwork!", LocalDateTime.now().minusDays(1), artists.get(0)),
                    new Post("Work in progress sketch", LocalDateTime.now().minusDays(2), artists.get(1)),
                    new Post("Character design drop 💥", LocalDateTime.now().minusDays(3), artists.get(2)),
                    new Post("Landscape art inspired by Ghibli", LocalDateTime.now().minusDays(4), artists.get(1)),
                    new Post("Color study from a reference photo", LocalDateTime.now().minusDays(5), artists.get(0))
            );

            postRepository.saveAll(posts);
        }
    }
}
