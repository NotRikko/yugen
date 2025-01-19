package rikko.yugen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.UserRepository;
import rikko.yugen.dto.ArtistCreateDTO;
import rikko.yugen.model.Artist;
import rikko.yugen.model.User;

@Service
public class ArtistService {
    
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private UserRepository userRepository;

    public Artist getArtistByName(String artistName) {
        return artistRepository.findByName(artistName)
            .orElseThrow(() -> new RuntimeException("Artist not found with name: " + artistName));
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Artist createArtist(ArtistCreateDTO artistCreateDTO) {
        artistRepository.findByName(artistCreateDTO.getName())
                .ifPresent(existingArtist -> {
                    throw new RuntimeException("Artist with name '" + existingArtist.getName() + "' already exists.");
                });

        User user = userRepository.findById(artistCreateDTO.getUserId())
                                .orElseThrow(() -> new RuntimeException("User not found"));

        Artist artist = new Artist();
        artist.setName(artistCreateDTO.getName());
        artist.setImage(artistCreateDTO.getImage());
        artist.setUser(user); 

        return artistRepository.save(artist);

    }
}
