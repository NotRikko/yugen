package rikko.yugen.controller;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<Page<ArtistDTO>> getAllArtists(
            @PageableDefault Pageable pageable
    ) {
        Page<ArtistDTO> artists = artistService.getAllArtists(pageable);
        return ResponseEntity.ok(artists);
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
    public ResponseEntity<ArtistDTO> createArtist(@Valid @RequestBody ArtistCreateDTO dto) {
        ArtistDTO createdArtist = artistService.createArtist(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdArtist);
    }

    // Posts

    @GetMapping("/{artistId}/posts")
    public ResponseEntity<Page<PostDTO>> getPostsByArtistId(
            @PathVariable Long artistId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(
                postService.getPostsByArtistId(artistId, pageable)
        );
    }

    // Products

    @GetMapping("/{artistId}/products")
    public ResponseEntity<Page<ProductDTO>> getProductsByArtistId(@PathVariable Long artistId, @PageableDefault Pageable pageable) {
        Page<ProductDTO> products = productService.getProductsByArtistId(artistId, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/me/products")
    public ResponseEntity<Page<ProductDTO>> getProductsOfLoggedInArtist(@PageableDefault Pageable pageable) {
        Page<ProductDTO> products = productService.getProductsOfCurrentArtist(pageable);
        return ResponseEntity.ok(products);
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