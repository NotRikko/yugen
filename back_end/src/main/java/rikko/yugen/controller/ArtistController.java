package rikko.yugen.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;


import rikko.yugen.dto.artist.ArtistCreateDTO;
import rikko.yugen.dto.artist.ArtistDTO;
import rikko.yugen.dto.follow.FollowWithUserDTO;
import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.model.Artist;
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

    @GetMapping("/")
    public ResponseEntity<List<ArtistDTO>> getAllArtists() {
        List<ArtistDTO> artists = artistService.getAllArtists()
                .stream()
                .map(ArtistDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/artist")
    public ResponseEntity<Artist> getArtist(@RequestParam String artistName) {
        Artist artist = artistService.getArtistByName(artistName);
        return ResponseEntity.ok(artist);
    }

    @GetMapping("/{artistName}")
    public ResponseEntity<ArtistDTO> getArtistByArtistName(@PathVariable String artistName) {
        ArtistDTO artist = artistService.getArtistByArtistName(artistName);
        return ResponseEntity.ok(artist);
    }

    @GetMapping("/{artistName}/posts")
    public List<PostDTO> getPostsByArtistName(@PathVariable String artistName) {
        return postService.getPostsByArtistName(artistName);
    }

    @GetMapping("/{artistId}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByArtistId(@PathVariable Long artistId) {
        List<ProductDTO> products = productService.getProductsByArtistId(artistId)
                .stream()
                .map(ProductDTO::fromProduct)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/me/products")
    public ResponseEntity<List<ProductDTO>> getProductsOfLoggedInArtist() {
        List<ProductDTO> products = productService.getProductsOfCurrentArtist();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/create")
    public ResponseEntity<ArtistDTO> createArtist(@RequestBody ArtistCreateDTO artistCreateDTO) {
        Artist createdArtist = artistService.createArtist(artistCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ArtistDTO(createdArtist));
    }

    @PostMapping("/{artistId}/followers")
    public ResponseEntity<FollowWithUserDTO> followArtist(
            @PathVariable Long artistId
    ) {
        FollowWithUserDTO follow = followService.followArtist(artistId);
        return ResponseEntity.status(HttpStatus.CREATED).body(follow);
    }

    @DeleteMapping("/{artistId}/followers")
    public ResponseEntity<Void> unfollowArtist(
            @PathVariable Long artistId
    ) {
        followService.unfollowArtist(artistId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{artistId}/followers")
    public ResponseEntity<List<FollowWithUserDTO>> getFollowers(
            @PathVariable Long artistId
    ) {
        List<FollowWithUserDTO> followers = followService.getFollowersForArtist(artistId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/artist/{artistId}/check")
    public ResponseEntity<Boolean> isFollowing(
            @PathVariable Long artistId
    ) {
        boolean following = followService.isFollowing(artistId);
        return ResponseEntity.ok(following);
    }
}