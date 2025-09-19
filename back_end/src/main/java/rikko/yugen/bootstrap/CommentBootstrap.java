package rikko.yugen.bootstrap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rikko.yugen.model.Post;
import rikko.yugen.model.User;
import rikko.yugen.model.Comment;
import rikko.yugen.repository.CommentRepository;
import rikko.yugen.repository.UserRepository;
import rikko.yugen.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CommentBootstrap {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentBootstrap(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void load() {
        if (commentRepository.count() == 0) {

            List<Post> posts = postRepository.findAll();
            List<User> users = userRepository.findAll();

            if (users.isEmpty()) {
                System.out.println("No users found. Cannot create posts.");
                return;
            }

            if (posts.isEmpty()) {
                System.out.println("No posts found. Cannot create posts.");
                return;
            }

            for (Post post : posts) {
                Comment comment = new Comment("Test!", users.get(randomIndex(users.size())), post);
                commentRepository.save(comment);
            }
        }
    }
    private int randomIndex(int size) {
        return (int) (Math.random() * size);
    }
}