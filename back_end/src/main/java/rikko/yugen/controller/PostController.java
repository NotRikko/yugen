package rikko.yugen.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rikko.yugen.dto.LikeDTO;
import rikko.yugen.dto.PostDTO;
import rikko.yugen.model.Post;
import rikko.yugen.service.PostService;
import rikko.yugen.service.LikeService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/posts")
public class PostController {
    
    @Autowired
    private PostService postService;

    @Autowired
    private LikeService likeService;

   @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        
        List<PostDTO> postDTOs = posts.stream()
                                      .map(post -> {
                                          Set<LikeDTO> likeDTOs = likeService.getLikesForPost(post.getId());
                                          return new PostDTO(post, likeDTOs);
                                      })
                                      .collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }
}
