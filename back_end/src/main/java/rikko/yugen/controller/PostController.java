package rikko.yugen.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import rikko.yugen.dto.PostDTO;
import rikko.yugen.service.PostService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/posts")
public class PostController {
    
    @Autowired
    private PostService postService;

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postDTOs = postService.getAllPosts()
        .stream()
        .map(PostDTO::new)
        .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }
}
