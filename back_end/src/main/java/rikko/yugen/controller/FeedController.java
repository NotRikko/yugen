package rikko.yugen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import rikko.yugen.dto.post.FeedPostDTO;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.service.FeedService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/global")
    public ResponseEntity<FeedService.FeedResponse<FeedPostDTO>> getGlobalFeed(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        FeedService.FeedResponse<FeedPostDTO> response = feedService.getGlobalFeed(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FeedService.FeedResponse<FeedPostDTO>> getUserFeed(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        FeedService.FeedResponse<FeedPostDTO> response = feedService.getUserFeed(pageable);
        return ResponseEntity.ok(response);
    }
}