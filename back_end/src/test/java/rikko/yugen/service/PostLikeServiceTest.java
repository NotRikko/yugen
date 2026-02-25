package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rikko.yugen.dto.like.PostLikeDTO;
import rikko.yugen.dto.like.PostLikeResponseDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Post;
import rikko.yugen.model.PostLike;
import rikko.yugen.model.User;
import rikko.yugen.repository.PostLikeRepository;
import rikko.yugen.repository.PostRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceTest {

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CurrentUserHelper currentUserHelper;

    @InjectMocks
    private PostLikeService postLikeService;

    // Mocks

    private Post post;
    private User user;
    private PostLike postLike;

    @BeforeEach
    void setUp() {

        post = new Post();
        post.setId(1L);

        user = new User();
        user.setId(1L);

        postLike = new PostLike();
        postLike.setId(1L);
        postLike.setUser(user);
        postLike.setPost(post);
    }

    // Get tests

    @Nested
    class GetPostLikeTests {
        @Test
        void getLikesForPost_shouldReturnLikes() {
            when(postRepository.findById(1L)).thenReturn(Optional.of(post));
            when(postLikeRepository.findByPost(post)).thenReturn(Set.of(postLike));

            Set<PostLikeDTO> likes = postLikeService.getLikesForPost(1L);

            assertEquals(1, likes.size());
            assertTrue(likes.stream().anyMatch(dto -> dto.user().id().equals(user.getId())));
        }

        @Test
        void getLikesForPost_shouldThrowIfPostNotFound() {
            when(postRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> postLikeService.getLikesForPost(1L));
        }

        @Test
        void getLikeCountForPost_shouldReturnCount() {
            when(postRepository.findById(1L)).thenReturn(Optional.of(post));
            when(postLikeRepository.countByPost(post)).thenReturn(5);

            int count = postLikeService.getLikeCountForPost(1L);

            assertEquals(5, count);
        }
    }

    // Toggle like tests

    @Nested
    class toggleLikeTests {

        @Test
        void toggleLike_shouldAddLikeIfNotPresent() {
            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(postRepository.findById(1L)).thenReturn(Optional.of(post));
            when(postLikeRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());
            when(postLikeRepository.countByPost(post)).thenReturn(1);
            when(postLikeRepository.save(any(PostLike.class))).thenAnswer(invocation -> invocation.getArgument(0));

            PostLikeResponseDTO response = postLikeService.toggleLikeAndReturnResponse(1L);

            assertEquals(1, response.likes());
            assertTrue(response.likedByCurrentUser());
            verify(postLikeRepository, times(1)).save(any(PostLike.class));
        }

        @Test
        void toggleLike_shouldRemoveLikeIfPresent() {
            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(postRepository.findById(1L)).thenReturn(Optional.of(post));
            when(postLikeRepository.findByUserAndPost(user, post)).thenReturn(Optional.of(postLike));
            when(postLikeRepository.countByPost(post)).thenReturn(0);

            PostLikeResponseDTO response = postLikeService.toggleLikeAndReturnResponse(1L);

            assertEquals(0, response.likes());
            assertFalse(response.likedByCurrentUser());
            verify(postLikeRepository, times(1)).delete(postLike);
        }

        @Test
        void toggleLike_shouldThrowIfPostNotFound() {
            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(postRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> postLikeService.toggleLikeAndReturnResponse(1L));
        }
    }
}
