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
import rikko.yugen.dto.artist.ArtistCreateDTO;
import rikko.yugen.dto.artist.ArtistDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.model.Artist;
import rikko.yugen.model.User;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.UserRepository;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {
    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private UserRepository userRepository;

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
        when(artistRepository.findById(1L)).thenReturn(Optional.of(mockArtist));
        ArtistDTO result = artistService.getArtistById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Rikko", result.artistName());
        assertEquals("I am a test", result.bio());
        verify(artistRepository).findById(1L);

    }

    @Test
    void getArtistById_shouldThrowException_whenArtistDoesNotExist() {
        when(artistRepository.findById(9999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> artistService.getArtistById(9999L));
        verify(artistRepository).findById(9999L);
    }


    //get by artist name tests
    @Test
    void getArtistByArtistName_shouldReturnArtistDTO_whenArtistExists() {
        when(artistRepository.findByArtistName("Rikko")).thenReturn(Optional.of(mockArtist));

        ArtistDTO result = artistService.getArtistByArtistName("Rikko");

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Rikko", result.artistName());
        assertEquals("I am a test", result.bio());
        verify(artistRepository).findByArtistName("Rikko");
    }

    @Test
    void getArtistByArtistName_shouldThrowException_whenArtistDoesNotExist() {
        when(artistRepository.findByArtistName("Rikko")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> artistService.getArtistByArtistName("Rikko"));
        verify(artistRepository).findByArtistName("Rikko");
    }

    //get all artists tests

    @Test
    void getAllArtists_shouldReturnPageOfArtistDTO_whenArtistsExists() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Artist> artistPage = new PageImpl<>(List.of(mockArtist, mockArtist2));
        when(artistRepository.findAll(pageable)).thenReturn(artistPage);
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

    //create artist tests

    @Test
    void createArtist_shouldReturnArtistDTO_whenArtistCreated() {
        // Arrange
        User mockUser = new User();
        mockUser.setId(3L);
        mockUser.setUsername("Rikko3");
        mockUser.setDisplayName("Rikko3");
        mockUser.setEmail("rikko3@test.com");

        ArtistCreateDTO dto = new ArtistCreateDTO();
        dto.setArtistName("Rikko3");
        dto.setUserId(3L);
        dto.setBio("I am a created test");

        when(userRepository.findById(3L)).thenReturn(Optional.of(mockUser));
        when(artistRepository.existsByArtistName("Rikko3")).thenReturn(false);

        Artist savedArtist = new Artist();
        savedArtist.setId(3L);
        savedArtist.setArtistName("Rikko3");
        savedArtist.setBio("I am a created test");
        savedArtist.setUser(mockUser);

        when(artistRepository.save(any(Artist.class))).thenReturn(savedArtist);

        ArtistDTO result = artistService.createArtist(dto);

        assertNotNull(result);
        assertEquals(3L, result.id());
        assertEquals("Rikko3", result.artistName());
        assertEquals("I am a created test", result.bio());
        assertEquals(3L, result.user().id());

        verify(userRepository).findById(3L);
        verify(artistRepository).existsByArtistName("Rikko3");
        verify(artistRepository).save(any(Artist.class));
    }

    // Delete artist tests

    @Test
    void deleteArtist_shouldReturnVoid_ifSuccess() {
        when(artistRepository.findById(1L)).thenReturn(Optional.of(mockArtist));
        artistService.deleteArtist(1L);
        verify(artistRepository).delete(mockArtist);
    }

    @Test
    void deleteArtist_shouldThrowException_whenArtistDoesNotExist() {
        when(artistRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> artistService.deleteArtist(1L));
        verify(userRepository, never()).delete(any());
    }


}
