package rikko.yugen.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import rikko.yugen.model.Follow;
import rikko.yugen.dto.follow.FollowDTO;
import rikko.yugen.service.FollowService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FollowDTO>> allFollowing(@PathVariable Long userId) {
        List<FollowDTO> following = followService.getAllFollowing(userId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/check/{userId}/{artistId}")
    public ResponseEntity<Boolean> isFollowing(
            @PathVariable Long userId,
            @PathVariable Long artistId
    ) {
        boolean following = followService.isFollowing(userId, artistId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<FollowDTO>> getFollowersForArtist(@PathVariable Long artistId) {
        List<FollowDTO> followers = followService.getFollowersForArtist(artistId);
        return ResponseEntity.ok(followers);
    }

    @PostMapping("/{userId}/{artistId}")
    public ResponseEntity<FollowDTO> followArtist(
            @PathVariable Long userId,
            @PathVariable Long artistId
    ) {
        FollowDTO follow = followService.followArtist(userId, artistId);
        return ResponseEntity.ok(follow);
    }

    @DeleteMapping("/{userId}/{artistId}")
    public ResponseEntity<Void> unfollowArtist(
            @PathVariable Long userId,
            @PathVariable Long artistId
    ) {
        followService.unfollowArtist(userId, artistId);
        return ResponseEntity.noContent().build();
    }
}