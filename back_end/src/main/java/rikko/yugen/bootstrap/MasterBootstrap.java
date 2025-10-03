package rikko.yugen.bootstrap;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.UserRepository;
import rikko.yugen.repository.CommentRepository;
import rikko.yugen.repository.ProductRepository;

@Component
public class MasterBootstrap implements CommandLineRunner {

    private final UserBootstrap userBootstrap;
    private final ArtistBootstrap artistBootstrap;
    private final ProductBootstrap productBootstrap;
    private final PostBootstrap postBootstrap;
    private final CommentBootstrap commentBootstrap;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final ProductRepository productRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public MasterBootstrap(UserBootstrap userBootstrap,
                           ArtistBootstrap artistBootstrap,
                           ProductBootstrap productBootstrap,
                           PostBootstrap postBootstrap,
                           CommentBootstrap commentBootstrap,
                           UserRepository userRepository,
                           ArtistRepository artistRepository,
                           ProductRepository productRepository,
                           PostRepository postRepository,
                           CommentRepository commentRepository) {
        this.userBootstrap = userBootstrap;
        this.artistBootstrap = artistBootstrap;
        this.productBootstrap = productBootstrap;
        this.postBootstrap = postBootstrap;
        this.commentBootstrap = commentBootstrap;
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
        this.productRepository = productRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        userBootstrap.load();
        System.out.println("Users loaded: " + userRepository.count());

        artistBootstrap.load();
        System.out.println("Artists loaded: " + artistRepository.count());

        productBootstrap.load();
        System.out.println("Products loaded: " + productRepository.count());

        postBootstrap.load();
        System.out.println("Posts loaded: " + postRepository.count());

        commentBootstrap.load();
        System.out.println("Comments loaded: " + commentRepository.count());


    }
}
