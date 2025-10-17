package rikko.yugen.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.UserRepository;
import rikko.yugen.dto.artist.ArtistCreateDTO;
import rikko.yugen.dto.artist.ArtistDTO;
import rikko.yugen.model.Artist;
import rikko.yugen.model.User;

@Service
@RequiredArgsConstructor
public class ArtistService {
    
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;

    public Artist getArtistById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with id: " + id));
    }

    public Artist getArtistByName(String artistName) {
        return artistRepository.findByArtistName(artistName)
            .orElseThrow(() -> new RuntimeException("Artist not found with name: " + artistName));
    }

    public ArtistDTO getArtistByArtistName(String artistName) {
        Artist artist = artistRepository.findByArtistName(artistName)
                .orElseThrow(() -> new RuntimeException("Artist not found with name: " + artistName));
        return new ArtistDTO(artist);
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
        artist.setProfilePictureUrl(artistCreateDTO.getProfilePictureUrl());
        artist.setBannerPictureUrl(artistCreateDTO.getBannerPictureUrl());
        artist.setUser(user); 

        return artistRepository.save(artist);

    }
}
