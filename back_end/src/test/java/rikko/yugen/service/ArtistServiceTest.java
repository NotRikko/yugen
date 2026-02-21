package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rikko.yugen.dto.artist.ArtistDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.model.Artist;
import rikko.yugen.model.User;
import rikko.yugen.repository.ArtistRepository;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {
    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistService artistService;

    //mock artist
    private Artist mockArtist ;
    private Artist mockArtist2;

    @BeforeEach

    void setUp() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("Rikko");
        mockUser.setDisplayName("Rikko");
        mockUser.setEmail("rikko@test.com");

        User mockUser2 = new User();
        mockUser.setId(2L);
        mockUser.setUsername("Rikko2");
        mockUser.setDisplayName("Rikko2");
        mockUser.setEmail("rikko2@test.com");

        mockArtist = new Artist();
        mockArtist.setId(1L);
        mockArtist.setArtistName("Rikko");
        mockArtist.setBio("I am a test");

        mockArtist.setUser(mockUser);
        mockUser.setIsArtist(true);
        mockUser.setArtist(mockArtist);

        mockArtist2 = new Artist();
        mockArtist2.setId(1L);
        mockArtist2.setArtistName("Rikko2");
        mockArtist2.setBio("I am a test2");

        mockArtist2.setUser(mockUser2);
        mockUser2.setIsArtist(true);
        mockUser2.setArtist(mockArtist2);
    }

    //get by id tests

    @Test
    void getArtistById_shouldReturnArtistDTO_whenArtistExists() {
        when(artistRepository.findById(1L))
                .thenReturn(Optional.of(mockArtist));
        ArtistDTO result = artistService.getArtistById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Rikko", result.artistName());
        assertEquals("I am a test", result.bio());
        verify(artistRepository).findById(1L);

    }

    @Test
    void getArtistById_shouldThrowException_whenArtistDoesNotExist() {
        when(artistRepository.findById(9999L))
            .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> artistService.getArtistById(9999L));
        verify(artistRepository).findById(9999L);
    }


    //get by artist name tests
    @Test
    void getArtistByArtistName_shouldReturnArtistDTO_whenArtistExists() {
        when(artistRepository.findByArtistName("Rikko")).
                thenReturn(Optional.of(mockArtist));

        ArtistDTO result = artistService.getArtistByArtistName("Rikko");

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Rikko", result.artistName());
        assertEquals("I am a test", result.bio());
        verify(artistRepository).findByArtistName("Rikko");
    }

    @Test
    void getArtistByArtistName_shouldThrowException_whenArtistDoesNotExist() {
        when(artistRepository.findByArtistName("Rikko")).
                thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> artistService.getArtistByArtistName("Rikko"));
        verify(artistRepository).findByArtistName("Rikko");
    }

    //get all artists tests

    @Test
    void getAllArtists_shouldReturnListOfArtistDTO_whenArtistsExists() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Artist> artistPage = new PageImpl<>(List.of(mockArtist, mockArtist2));
        when(artistRepository.findAll(pageable))
                    .thenReturn(artistPage);
        Page<ArtistDTO> result = artistService.getAllArtists(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        ArtistDTO first = result.getContent().get(0);
        assertEquals("Rikko", first.artistName());
        assertEquals("I am a test", first.bio());

        ArtistDTO second = result.getContent().get(1);
        assertEquals("Rikko2", second.artistName());
        assertEquals("I am a test2", second.bio());

        verify(artistRepository).findAll(pageable);
    }



}
