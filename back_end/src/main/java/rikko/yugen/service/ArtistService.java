package rikko.yugen.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rikko.yugen.dto.user.UserDTO;
import rikko.yugen.exception.ResourceAlreadyExistsException;
import rikko.yugen.exception.ResourceNotFoundException;
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

    // Mapping
    private ArtistDTO artistToArtistDTO(Artist artist) {
        return new ArtistDTO(
                artist.getId(),
                artist.getArtistName(),
                artist.getProfilePictureUrl(),
                artist.getBannerPictureUrl(),
                artist.getUser() != null ? new UserDTO(artist.getUser()) : null
        );
    }

    // Read Operations

    public ArtistDTO getArtistById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artist", "id", id));

        return artistToArtistDTO(artist);
    }

    public ArtistDTO getArtistByName(String artistName) {
        Artist artist = artistRepository.findByArtistName(artistName)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artist", "artistName", artistName));

        return artistToArtistDTO(artist);
    }

    public Page<ArtistDTO> getAllArtists(Pageable pageable) {
        return artistRepository.findAll(pageable)
                .map(this::artistToArtistDTO);
    }

    // Create

    @Transactional
    public ArtistDTO createArtist(ArtistCreateDTO dto) {

        String normalizedName = dto.getArtistName().trim();

        if (artistRepository.existsByArtistName(normalizedName)) {
            throw new ResourceAlreadyExistsException("Artist", "artistName", normalizedName);
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", dto.getUserId()));

        Artist artist = new Artist();
        artist.setArtistName(normalizedName);
        artist.setProfilePictureUrl(dto.getProfilePictureUrl());
        artist.setBannerPictureUrl(dto.getBannerPictureUrl());
        artist.setUser(user);

        try {
            Artist savedArtist = artistRepository.save(artist);
            return artistToArtistDTO(savedArtist);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistsException("Artist", "artistName", normalizedName);
        }
    }
}