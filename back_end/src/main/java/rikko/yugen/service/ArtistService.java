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

    public Artist getArtistByName(String artistName) {
        return artistRepository.findByName(artistName)
            .orElseThrow(() -> new RuntimeException("Artist not found with name: " + artistName));
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Artist createArtist(Artist artist) {
        artistRepository.findByName(artist.getName())
                .ifPresent(existingArtist -> {
                    throw new RuntimeException("Artist with name '" + existingArtist.getName() + "' already exists.");
                });

        return artistRepository.save(artist);
    }


}
