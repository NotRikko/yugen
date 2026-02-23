package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rikko.yugen.dto.follow.FollowWithUserDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Follow;
import rikko.yugen.model.FollowId;
import rikko.yugen.model.User;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.FollowRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private CurrentUserHelper currentUserHelper;

    @InjectMocks
    private FollowService followService;

    private User mockUser;
    private Artist mockArtist;
    private Follow mockFollow;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("Rikko");

        mockArtist = new Artist();
        mockArtist.setId(1L);
        mockArtist.setArtistName("CoolArtist");

        User artistUser = new User();
        artistUser.setId(2L);
        artistUser.setUsername("ArtistUser");
        mockArtist.setUser(artistUser);

        mockFollow = new Follow();
        mockFollow.setFollower(mockUser);
        mockFollow.setFollowee(mockArtist);
        mockFollow.setFollowedAt(LocalDateTime.now());
        mockFollow.setId(new FollowId(mockUser.getId(), mockArtist.getId()));
    }

    // Get tests
    @Nested
    class GetFollowTests {

        @Test
        void getFollowingForCurrentUser_shouldReturnList() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(followRepository.findByFollowerId(mockUser.getId())).thenReturn(List.of(mockFollow));

            List<FollowWithUserDTO> result = followService.getFollowingForCurrentUser();

            assertEquals(1, result.size());
            assertEquals(mockArtist.getId(), result.get(0).id());
        }

        @Test
        void getFollowersForArtist_shouldReturnList() {
            when(followRepository.findByFolloweeId(mockArtist.getId())).thenReturn(List.of(mockFollow));

            List<FollowWithUserDTO> result = followService.getFollowersForArtist(mockArtist.getId());

            assertEquals(1, result.size());
            assertEquals(mockUser.getId(), result.get(0).id());
        }
    }

    // Follow tests
    @Nested
    class FollowArtistTests {

        @Test
        void followArtist_shouldSaveNewFollow() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(artistRepository.findById(mockArtist.getId())).thenReturn(Optional.of(mockArtist));
            when(followRepository.findById(mockFollow.getId())).thenReturn(Optional.empty());
            when(followRepository.save(any(Follow.class))).thenAnswer(i -> i.getArguments()[0]);

            FollowWithUserDTO result = followService.followArtist(mockArtist.getId());

            assertEquals(mockArtist.getId(), result.id());
            verify(followRepository, times(1)).save(any(Follow.class));
        }

        @Test
        void followArtist_shouldThrow_whenArtistNotFound() {
            when(artistRepository.findById(99L)).thenReturn(Optional.empty());
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);

            assertThrows(ResourceNotFoundException.class,
                    () -> followService.followArtist(99L));
        }

        @Test
        void followArtist_shouldThrow_whenUserFollowsSelf() {
            mockArtist.setUser(mockUser);
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(artistRepository.findById(mockArtist.getId())).thenReturn(Optional.of(mockArtist));

            assertThrows(IllegalStateException.class,
                    () -> followService.followArtist(mockArtist.getId()));
        }
    }

    // Unfollow tests

    @Nested
    class UnfollowArtistTests {

        @Test
        void unfollowArtist_shouldDeleteFollow() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(followRepository.existsById(mockFollow.getId())).thenReturn(true);

            followService.unfollowArtist(mockArtist.getId());

            verify(followRepository, times(1)).deleteById(mockFollow.getId());
        }

        @Test
        void unfollowArtist_shouldThrow_whenFollowNotFound() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(followRepository.existsById(mockFollow.getId())).thenReturn(false);

            assertThrows(ResourceNotFoundException.class,
                    () -> followService.unfollowArtist(mockArtist.getId()));

            verify(followRepository, never()).deleteById(any());
        }
    }

    // Check following tests

    @Nested
    class IsFollowingTests {

        @Test
        void isFollowing_shouldReturnTrue_whenExists() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(followRepository.existsById(mockFollow.getId())).thenReturn(true);

            boolean result = followService.isFollowing(mockArtist.getId());

            assertTrue(result);
        }

        @Test
        void isFollowing_shouldReturnFalse_whenNotExists() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(followRepository.existsById(mockFollow.getId())).thenReturn(false);

            boolean result = followService.isFollowing(mockArtist.getId());

            assertFalse(result);
        }
    }
}