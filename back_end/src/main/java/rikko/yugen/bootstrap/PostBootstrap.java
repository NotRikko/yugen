package rikko.yugen.bootstrap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Post;
import rikko.yugen.model.Image;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.ImageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PostBootstrap {

    private final PostRepository postRepository;
    private final ArtistRepository artistRepository;
    private final ImageRepository imageRepository;

    public PostBootstrap(PostRepository postRepository, ArtistRepository artistRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.artistRepository = artistRepository;
        this.imageRepository = imageRepository;
    }

    public void load() {
        if (postRepository.count() == 0) {
            List<Artist> artists = artistRepository.findAll();

            if (artists.isEmpty()) {
                System.out.println("No artists found. Cannot create posts.");
                return;
            }

            List<Post> posts = List.of(
                    new Post("This is me if anyone even cares.", LocalDateTime.now().minusDays(1), artists.get(0)),
                    new Post("Felt pretty.", LocalDateTime.now().minusDays(2), artists.get(1)),
                    new Post("Character design drop 💥", LocalDateTime.now().minusDays(3), artists.get(2)),
                    new Post("Landscape art inspired by Ghibli", LocalDateTime.now().minusDays(4), artists.get(1)),
                    new Post("Color study from a reference photo", LocalDateTime.now().minusDays(5), artists.get(0))
            );

            postRepository.saveAll(posts);

            List<Image> images = List.of(
                    createImageForPost(posts.get(0), "https://preview.redd.it/sandrone-has-the-best-design-in-genshin-impact-even-for-a-v0-nhwu0ocd1r3e1.jpeg?auto=webp&s=0f977dadd3c544f05291a742d460dfbbd555fa15"),
                    createImageForPost(posts.get(1), "https://preview.redd.it/columbina-fanart-v0-3r4bud3hp3sb1.jpg?auto=webp&s=558d1392a259475c16b5f48c9b2ebc970be61283"),
                    createImageForPost(posts.get(1), "https://preview.redd.it/columbina-fan-art-v0-enci6vojneod1.jpg?width=1080&crop=smart&auto=webp&s=09a49a67c317e72fe1775aa5f773e190517b1243")
            );

            imageRepository.saveAll(images);
        }
    }
    private Image createImageForPost(Post post, String url) {
        Image image = new Image();
        image.setPost(post);
        image.setUrl(url);
        post.getImages().add(image);
        return image;
    }
}
