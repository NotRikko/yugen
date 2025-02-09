package rikko.yugen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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
        return artistRepository.findByArtistName(artistName)
            .orElseThrow(() -> new RuntimeException("Artist not found with name: " + artistName));
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    @Transactional
    public Artist createArtist(ArtistCreateDTO artistCreateDTO) {
        artistRepository.findByArtistName(artistCreateDTO.getArtistName())
                .ifPresent(existingArtist -> {
                    throw new RuntimeException("Artist with name '" + existingArtist.getArtistName() + "' already exists.");
                });

        User user = userRepository.findById(artistCreateDTO.getUserId())
                                .orElseThrow(() -> new RuntimeException("User not found"));

        Artist artist = new Artist();
        artist.setArtistName(artistCreateDTO.getArtistName());
        artist.setImage(artistCreateDTO.getImage());
        artist.setUser(user); 

        return artistRepository.save(artist);

    }
}
