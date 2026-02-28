package rikko.yugen.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import rikko.yugen.dto.PageResponseDTO;
import rikko.yugen.dto.artist.ArtistCreateDTO;
import rikko.yugen.dto.artist.ArtistDTO;
import rikko.yugen.dto.follow.FollowWithUserDTO;
import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.service.ArtistService;
import rikko.yugen.service.ProductService;
import rikko.yugen.service.PostService;
import rikko.yugen.service.FollowService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;
    private final ProductService productService;
    private final PostService postService;
    private final FollowService followService;

    // Artists

    @GetMapping
    public PageResponseDTO<ArtistDTO> getAllArtists(@PageableDefault Pageable pageable) {
        Page<ArtistDTO> page = artistService.getAllArtists(pageable);
        return new PageResponseDTO<>(page);
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistDTO> getArtistById(@PathVariable Long artistId) {
        ArtistDTO artist = artistService.getArtistById(artistId);
        return ResponseEntity.ok(artist);
    }

    @GetMapping("/by-name/{artistName}")
    public ResponseEntity<ArtistDTO> getArtistByName(@PathVariable String artistName) {
        ArtistDTO artist = artistService.getArtistByArtistName(artistName);
        return ResponseEntity.ok(artist);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ArtistDTO> createArtist(@Valid @RequestBody ArtistCreateDTO dto) {
        ArtistDTO createdArtist = artistService.createArtist(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdArtist);
    }

    // Posts

    @GetMapping("/{artistId}/posts")
    public PageResponseDTO<PostDTO> getPostsByArtistId(
            @PathVariable Long artistId,
            @PageableDefault(sort = "createdAt",
                    direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<PostDTO> page =
                postService.getPostsByArtistId(artistId, pageable);

        return new PageResponseDTO<>(page);
    }

    // Products

    @GetMapping("/{artistId}/products")
    public PageResponseDTO<ProductDTO> getProductsByArtistId(
            @PathVariable Long artistId,
            @PageableDefault Pageable pageable) {

        Page<ProductDTO> page =
                productService.getProductsByArtistId(artistId, pageable);

        return new PageResponseDTO<>(page);
    }

    @GetMapping("/me/products")
    public PageResponseDTO<ProductDTO> getProductsOfLoggedInArtist(
            @PageableDefault Pageable pageable) {

        Page<ProductDTO> page =
                productService.getProductsOfCurrentArtist(pageable);

        return new PageResponseDTO<>(page);
    }

    // Followers

    @PostMapping("/{artistId}/followers")
    public ResponseEntity<FollowWithUserDTO> followArtist(@PathVariable Long artistId) {
        FollowWithUserDTO follow = followService.followArtist(artistId);
        return ResponseEntity.status(HttpStatus.CREATED).body(follow);
    }

    @DeleteMapping("/{artistId}/followers")
    public ResponseEntity<Void> unfollowArtist(@PathVariable Long artistId) {
        followService.unfollowArtist(artistId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{artistId}/followers")
    public ResponseEntity<List<FollowWithUserDTO>> getFollowers(@PathVariable Long artistId) {
        List<FollowWithUserDTO> followers = followService.getFollowersForArtist(artistId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{artistId}/followers/me")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long artistId) {
        boolean following = followService.isFollowing(artistId);
        return ResponseEntity.ok(following);
    }
}