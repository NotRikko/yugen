package rikko.yugen.controller;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import lombok.RequiredArgsConstructor;

import rikko.yugen.model.Follow;
import rikko.yugen.model.User;
import rikko.yugen.dto.follow.FollowDTO;
import rikko.yugen.service.FollowService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {


    private final FollowService followService;

    @PostMapping("/artist/{artistId}")
    public ResponseEntity<FollowDTO> followArtist(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long artistId
    ) {
        FollowDTO follow = followService.followArtist(currentUser.getId(), artistId);
        return ResponseEntity.ok(follow);
    }

    @DeleteMapping("/artist/{artistId}")
    public ResponseEntity<Void> unfollowArtist(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long artistId
    ) {
        followService.unfollowArtist(currentUser.getId(), artistId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/artist/{artistId}/check")
    public ResponseEntity<Boolean> isFollowing(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long artistId
    ) {
        boolean following = followService.isFollowing(currentUser.getId(), artistId);
        return ResponseEntity.ok(following);
    }
}