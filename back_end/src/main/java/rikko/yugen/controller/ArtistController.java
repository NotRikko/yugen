package rikko.yugen.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import rikko.yugen.dto.ArtistDTO;
import rikko.yugen.dto.ArtistCreateDTO;
import rikko.yugen.model.Artist;
import rikko.yugen.service.ArtistService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
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
    public ResponseEntity<List<ArtistDTO>> getAllArtists() {
        List<ArtistDTO> artistDTOs = artistService.getAllArtists()
        .stream()
        .map(ArtistDTO::new)
        .collect(Collectors.toList());
        return ResponseEntity.ok(artistDTOs);
    }
    

    @PostMapping("/create")
    public ResponseEntity<ArtistDTO> createArtist(@RequestBody ArtistCreateDTO artistCreateDTO) {
        Artist createdArtist = artistService.createArtist(artistCreateDTO);

        ArtistDTO artistDTO = new ArtistDTO(createdArtist);

        return ResponseEntity.status(HttpStatus.CREATED).body(artistDTO);
    }
    
}
