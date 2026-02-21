package rikko.yugen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.service.FeedService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/global")
    public ResponseEntity<FeedService.FeedResponse<PostDTO>> getGlobalFeed(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        FeedService.FeedResponse<PostDTO> response = feedService.getGlobalFeed(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<FeedService.FeedResponse<PostDTO>> getUserFeed(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        FeedService.FeedResponse<PostDTO> response = feedService.getUserFeed(pageable);
        return ResponseEntity.ok(response);
    }
}