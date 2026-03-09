package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Role;
import rikko.yugen.model.User;
import rikko.yugen.repository.ArtistRepository;

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
    private CurrentUserHelper currentUserHelper;

    @InjectMocks
    private ArtistService artistService;

    //Mock artist

    private Artist artist ;
    private Artist artist2;

    @BeforeEach
    void setUp() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("Rikko");
        mockUser.setDisplayName("Rikko");
        mockUser.setEmail("rikko@test.com");
        mockUser.setRole(Role.ARTIST);

        User mockUser2 = new User();
        mockUser2.setId(2L);
        mockUser2.setUsername("Rikko2");
        mockUser2.setDisplayName("Rikko2");
        mockUser2.setEmail("rikko2@test.com");
        mockUser2.setRole(Role.ARTIST);

        artist = new Artist();
        artist.setId(1L);
        artist.setBio("I am a test");
        artist.setUser(mockUser);
        mockUser.setIsArtist(true);
        mockUser.setArtist(artist);

        artist2 = new Artist();
        artist2.setId(2L);
        artist2.setBio("I am a test2");
        artist2.setUser(mockUser2);
        mockUser2.setIsArtist(true);
        mockUser2.setArtist(artist2);
    }

    //Get tests

    @Nested
    class GetArtistTests {

        @Test
        void shouldReturnArtistDTO_whenArtistExistsById() {
            when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

            ArtistDTO result = artistService.getArtistById(1L);

            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("I am a test", result.bio());

            verify(artistRepository).findById(1L);
        }

        @Test
        void shouldThrowException_whenArtistDoesNotExistById() {
            when(artistRepository.findById(9999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> artistService.getArtistById(9999L));

            verify(artistRepository).findById(9999L);
        }

        @Test
        void shouldReturnArtistDTO_whenArtistExistsByUsername() {
            when(artistRepository.findByUser_Username("Rikko")).thenReturn(Optional.of(artist));

            ArtistDTO result = artistService.getArtistByUsername("Rikko");

            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("I am a test", result.bio());

            verify(artistRepository).findByUser_Username("Rikko");
        }

        @Test
        void shouldThrowException_whenArtistDoesNotExistByName() {
            when(artistRepository.findByUser_Username("Rikko")).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> artistService.getArtistByUsername("Rikko"));

            verify(artistRepository).findByUser_Username("Rikko");
        }

        @Test
        void shouldReturnPaginatedArtists_whenArtistsExist() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Artist> artistPage = new PageImpl<>(List.of(artist, artist2));
            when(artistRepository.findAll(pageable)).thenReturn(artistPage);

            Page<ArtistDTO> result = artistService.getAllArtists(pageable);

            assertNotNull(result);
            assertEquals(2, result.getContent().size());
            assertEquals("I am a test2", result.getContent().get(1).bio());

            verify(artistRepository).findAll(pageable);
        }
    }

    // Create artist tests

    @Nested
    class CreateArtistTests {

        @Test
        void shouldCreateArtist_whenValid() {
            User mockUser = new User();
            mockUser.setId(3L);
            mockUser.setUsername("Rikko3");
            mockUser.setDisplayName("Rikko3");
            mockUser.setEmail("rikko3@test.com");
            mockUser.setRole(Role.USER);

            ArtistCreateDTO dto = new ArtistCreateDTO();
            dto.setBio("I am a created test");

            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(artistRepository.existsByUser_Id(3L)).thenReturn(false);

            Artist savedArtist = new Artist();
            savedArtist.setId(3L);
            savedArtist.setBio("I am a created test");
            savedArtist.setUser(mockUser);

            when(artistRepository.save(any(Artist.class))).thenReturn(savedArtist);

            ArtistDTO result = artistService.createArtist(dto);

            assertNotNull(result);
            assertEquals(3L, result.id());
            assertEquals("I am a created test", result.bio());
            assertEquals(3L, result.userId());

            verify(artistRepository).existsByUser_Id(3L);
            verify(artistRepository).save(any(Artist.class));
        }
    }

    // Delete artist tests

    @Nested
    class DeleteArtistTests {

        @Test
        void shouldDeleteArtist_whenArtistExists() {
            when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

            artistService.deleteArtist(1L);

            verify(artistRepository).delete(artist);
        }

        @Test
        void shouldThrowException_whenArtistDoesNotExist() {
            when(artistRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> artistService.deleteArtist(1L));

            verify(artistRepository, never()).delete(any());
        }
    }

}
