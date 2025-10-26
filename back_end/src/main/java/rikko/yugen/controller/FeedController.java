package rikko.yugen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.service.FeedService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final CurrentUserHelper currentUserHelper;

    @GetMapping("/global")
    public ResponseEntity<List<PostDTO>> getGlobalFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<PostDTO> posts = feedService.getGlobalFeed(page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user")
    public ResponseEntity<List<PostDTO>> getUserFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Long currentUserId = currentUserHelper.getCurrentUser().getId();
        List<PostDTO> posts = feedService.getUserFeed(currentUserId, page, size);
        return ResponseEntity.ok(posts);
    }
}