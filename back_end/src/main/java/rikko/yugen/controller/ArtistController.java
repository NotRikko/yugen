package rikko.yugen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import rikko.yugen.model.Artist;
import rikko.yugen.service.ArtistService;

@RestController
@RequestMapping("/artists")
public class ArtistController {
    
    @Autowired
    private ArtistService artistService;

    @GetMapping("/artist")
    public ResponseEntity<Artist> getArtist(@RequestParam String name) {
        Artist artist = artistService.getArtistByName(name);
        if (artist == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found with name: " + name);
        }
        return ResponseEntity.ok(artist);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Artist>> getAllArtists() {
        List<Artist> artists = artistService.getAllArtists();
        return ResponseEntity.ok(artists);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createArtist(@RequestBody request) {
        artistService.create(request.)
    }
}
