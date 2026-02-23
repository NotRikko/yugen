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
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Follow;
import rikko.yugen.model.Post;
import rikko.yugen.model.User;
import rikko.yugen.repository.FollowRepository;
import rikko.yugen.repository.PostRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private CurrentUserHelper currentUserHelper;

    @InjectMocks
    private FeedService feedService;

    // Mocks
    private Post post;
    private User user;
    private Artist followedArtist;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(1L);

        user = new User();
        user.setId(1L);

        followedArtist = new Artist();
        followedArtist.setId(2L);
    }

    // Global feed tests

    @Nested
    class GlobalFeedTests {

        @Test
        void getGlobalFeed_shouldReturnPosts() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Post> postPage = new PageImpl<>(List.of(post));

            when(postRepository.findAll(pageable)).thenReturn(postPage);

            FeedService.FeedResponse<PostDTO> response = feedService.getGlobalFeed(pageable);

            assertEquals(1, response.posts().size());
            assertEquals(post.getId(), response.posts().get(0).id());
            assertFalse(response.hasNext());
        }

        @Test
        void getGlobalFeed_shouldReturnEmptyIfNoPosts() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Post> emptyPage = new PageImpl<>(Collections.emptyList());

            when(postRepository.findAll(pageable)).thenReturn(emptyPage);

            FeedService.FeedResponse<PostDTO> response = feedService.getGlobalFeed(pageable);

            assertTrue(response.posts().isEmpty());
            assertFalse(response.hasNext());
        }
    }

    // User feed tests
    @Nested
    class UserFeedTests {

        @Test
        void getUserFeed_shouldReturnPostsFromFollowedArtists() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Post> postPage = new PageImpl<>(List.of(post));

            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(followRepository.findByFollowerId(user.getId()))
                    .thenReturn(List.of(new Follow(user, followedArtist)));
            when(postRepository.findByArtist_IdIn(List.of(followedArtist.getId()), pageable))
                    .thenReturn(postPage);

            FeedService.FeedResponse<PostDTO> response = feedService.getUserFeed(pageable);

            assertEquals(1, response.posts().size());
            assertEquals(post.getId(), response.posts().get(0).id());
            assertFalse(response.hasNext());
        }

        @Test
        void getUserFeed_shouldReturnEmptyIfNoFollowedArtists() {
            Pageable pageable = PageRequest.of(0, 10);

            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(followRepository.findByFollowerId(user.getId())).thenReturn(Collections.emptyList());

            FeedService.FeedResponse<PostDTO> response = feedService.getUserFeed(pageable);

            assertTrue(response.posts().isEmpty());
            assertFalse(response.hasNext());
        }

        @Test
        void getUserFeed_shouldReturnEmptyIfNoPostsFromFollowedArtists() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Post> emptyPage = new PageImpl<>(Collections.emptyList());

            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(followRepository.findByFollowerId(user.getId()))
                    .thenReturn(List.of(new Follow(user, followedArtist)));
            when(postRepository.findByArtist_IdIn(List.of(followedArtist.getId()), pageable))
                    .thenReturn(emptyPage);

            FeedService.FeedResponse<PostDTO> response = feedService.getUserFeed(pageable);

            assertTrue(response.posts().isEmpty());
            assertFalse(response.hasNext());
        }
    }
}