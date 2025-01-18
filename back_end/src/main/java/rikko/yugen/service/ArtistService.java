package rikko.yugen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.model.Artist;

@Service
public class ArtistService {
    
    @Autowired
    private ArtistRepository artistRepository;

    public Artist getArtistByName(String name) {
        return artistRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("Artist not found with name: " + name));
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Artist createArtist(Artist artist) {
        artistRepository.findByName(artist.getArtistName())
                .ifPresent(existingArtist -> {
                    throw new RuntimeException("Artist with name '" + existingArtist.getArtistName() + "' already exists.");
                });

        return artistRepository.save(artist);
    }


}
