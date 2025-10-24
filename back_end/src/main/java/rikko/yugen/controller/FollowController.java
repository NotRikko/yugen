package rikko.yugen.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import lombok.RequiredArgsConstructor;

import rikko.yugen.model.User;
import rikko.yugen.dto.follow.FollowWithUserDTO;
import rikko.yugen.service.FollowService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping("/artist/{artistId}/followers")
    public ResponseEntity<List<FollowWithUserDTO>> getFollowers(
            @PathVariable Long artistId
    ) {
        List<FollowWithUserDTO> followers = followService.getFollowersForArtist(artistId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/user/{userId}/following")
    public ResponseEntity<List<FollowWithUserDTO>> getFollowing(
            @PathVariable Long userId
    ) {
        List<FollowWithUserDTO> followees = followService.getAllFolloweesForUser(userId);
        return ResponseEntity.ok(followees);
    }

    @PostMapping("/artist/{artistId}")
    public ResponseEntity<FollowWithUserDTO> followArtist(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long artistId
    ) {
        FollowWithUserDTO follow = followService.followArtist(currentUser.getId(), artistId);
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