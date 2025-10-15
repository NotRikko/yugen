package rikko.yugen.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;


import rikko.yugen.dto.artist.ArtistCreateDTO;
import rikko.yugen.dto.artist.ArtistDTO;
import rikko.yugen.dto.product.ProductDTO;
import rikko.yugen.model.Artist;
import rikko.yugen.service.ArtistService;
import rikko.yugen.service.ProductService;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping("/artist")
    public ResponseEntity<Artist> getArtist(@RequestParam String artistName) {
        Artist artist = artistService.getArtistByName(artistName);
        if (artist == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found with name: " + artistName);
        }
        return ResponseEntity.ok(artist);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ArtistDTO>> getAllArtists() {
        List<ArtistDTO> artistDTOs = artistService.getAllArtists()
        .stream()
        .map(ArtistDTO::new)
        .collect(Collectors.toList());
        return ResponseEntity.ok(artistDTOs);
    }

    @GetMapping("/{artistId}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByArtistId(@PathVariable Long artistId) {
        List<ProductDTO> products = productService.getProductsByArtist(artistId);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/create")
    public ResponseEntity<ArtistDTO> createArtist(@RequestBody ArtistCreateDTO artistCreateDTO) {
        Artist createdArtist = artistService.createArtist(artistCreateDTO);

        ArtistDTO artistDTO = new ArtistDTO(createdArtist);

        return ResponseEntity.status(HttpStatus.CREATED).body(artistDTO);
    }

}
