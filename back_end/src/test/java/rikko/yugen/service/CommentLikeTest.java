package rikko.yugen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rikko.yugen.dto.like.CommentLikeDTO;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Comment;
import rikko.yugen.model.CommentLike;
import rikko.yugen.model.User;
import rikko.yugen.repository.CommentLikeRepository;
import rikko.yugen.repository.CommentRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentLikeServiceTest {

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CurrentUserHelper currentUserHelper;

    @InjectMocks
    private CommentLikeService commentLikeService;

    // Mocks

    private Comment comment;
    private User user;
    private CommentLike commentLike;

    @BeforeEach
    void setUp() {
        comment = new Comment();
        comment.setId(1L);

        user = new User();
        user.setId(1L);

        commentLike = new CommentLike();
        commentLike.setId(1L);
        commentLike.setUser(user);
        commentLike.setComment(comment);
    }

    // Get tests

    @Nested
    class GetCommentLikeTests {
        @Test
        void getLikesForComment_shouldReturnLikes() {
            when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
            when(commentLikeRepository.findByComment(comment)).thenReturn(Set.of(commentLike));

            Set<CommentLikeDTO> likes = commentLikeService.getLikesForComment(1L);

            assertEquals(1, likes.size());
            assertTrue(likes.stream().anyMatch(dto -> dto.user().id().equals(user.getId())));
        }

        @Test
        void getLikesForComment_shouldThrowIfCommentNotFound() {
            when(commentRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> commentLikeService.getLikesForComment(1L));
        }

        @Test
        void getLikeCountForComment_shouldReturnCount() {
            when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
            when(commentLikeRepository.countByComment(comment)).thenReturn(5);

            int count = commentLikeService.getLikeCountForComment(1L);

            assertEquals(5, count);
        }
    }

    // Toggle like tests

    @Nested
    class ToggleLikeTests {
        @Test
        void toggleLike_shouldAddLikeIfNotPresent() {
            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
            when(commentLikeRepository.findByUserAndComment(user, comment)).thenReturn(Optional.empty());
            when(commentLikeRepository.countByComment(comment)).thenReturn(1);
            when(commentLikeRepository.save(any(CommentLike.class))).thenAnswer(invocation -> invocation.getArgument(0));

            int count = commentLikeService.toggleLike(1L);

            assertEquals(1, count);
            verify(commentLikeRepository, times(1)).save(any(CommentLike.class));
        }

        @Test
        void toggleLike_shouldRemoveLikeIfPresent() {
            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
            when(commentLikeRepository.findByUserAndComment(user, comment)).thenReturn(Optional.of(commentLike));
            when(commentLikeRepository.countByComment(comment)).thenReturn(0);

            int count = commentLikeService.toggleLike(1L);

            assertEquals(0, count);
            verify(commentLikeRepository, times(1)).delete(commentLike);
        }

        @Test
        void toggleLike_shouldThrowIfCommentNotFound() {
            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(commentRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> commentLikeService.toggleLike(1L));
        }
    }
}