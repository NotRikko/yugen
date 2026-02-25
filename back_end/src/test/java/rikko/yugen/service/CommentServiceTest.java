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
import org.springframework.security.access.AccessDeniedException;
import rikko.yugen.dto.comment.CommentCreateDTO;
import rikko.yugen.dto.comment.CommentDTO;
import rikko.yugen.dto.comment.CommentUpdateDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Comment;
import rikko.yugen.model.Post;
import rikko.yugen.model.User;
import rikko.yugen.repository.CommentRepository;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserHelper currentUserHelper;

    @InjectMocks
    private CommentService commentService;

    private User mockUser;
    private Post mockPost;
    private Comment mockComment;

    @BeforeEach
    void setUp() {

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("Rikko");

        mockPost = new Post();
        mockPost.setId(1L);

        mockComment = new Comment();
        mockComment.setId(1L);
        mockComment.setUser(mockUser);
        mockComment.setPost(mockPost);
        mockComment.setContent("Hello world");
    }

    // Get tests

    @Nested
    class GetCommentTests {
        @Test
        void getCommentsByPostId_shouldReturnPage() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Comment> page = new PageImpl<>(List.of(mockComment));

            when(commentRepository.findByPostId(1L, pageable)).thenReturn(page);

            Page<CommentDTO> result = commentService.getCommentsByPostId(1L, pageable);

            assertEquals(1, result.getTotalElements());
            assertEquals("Hello world", result.getContent().get(0).content());
        }

        @Test
        void getCommentsByPostId_shouldThrow_whenPostDoesNotExist() {
            Pageable pageable = PageRequest.of(0, 10);
            when(commentRepository.findByPostId(1L, pageable)).thenReturn(Page.empty());
            when(postRepository.existsById(1L)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class,
                    () -> commentService.getCommentsByPostId(1L, pageable));
        }

        @Test
        void getCommentsByUserId_shouldReturnPage() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Comment> page = new PageImpl<>(List.of(mockComment));
            when(userRepository.existsById(1L)).thenReturn(true);
            when(commentRepository.findByUserId(1L, pageable)).thenReturn(page);

            Page<CommentDTO> result = commentService.getCommentsByUserId(1L, pageable);
            assertEquals(1, result.getTotalElements());
            assertEquals("Hello world", result.getContent().get(0).content());
        }

        @Test
        void getCommentsByUserId_shouldThrow_whenUserDoesNotExist() {
            Pageable pageable = PageRequest.of(0, 10);
            when(userRepository.existsById(1L)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class,
                    () -> commentService.getCommentsByUserId(1L, pageable));
        }
    }

    // Create tests
    @Nested
    class CreateCommentTests {
        @Test
        void createComment_shouldSaveAndReturnCommentDTO() {
            CommentCreateDTO dto = new CommentCreateDTO();
            dto.setPostId(1L);
            dto.setContent("New comment");

            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));
            when(commentRepository.save(any(Comment.class))).thenAnswer(i -> i.getArguments()[0]);

            CommentDTO result = commentService.createComment(dto);

            assertEquals("New comment", result.content());
            assertEquals(mockUser.getId(), result.user().id());
            assertEquals(mockPost.getId(), result.postId());
            verify(commentRepository, times(1)).save(any(Comment.class));
        }

        @Test
        void createComment_shouldThrow_whenPostNotFound() {
            CommentCreateDTO dto = new CommentCreateDTO();
            dto.setPostId(99L);
            dto.setContent("Test");

            when(postRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> commentService.createComment(dto));
        }
    }


    // Update comment tests

    @Nested
    class UpdateCommentTests {
        @Test
        void updateComment_shouldUpdateContent_whenOwner() {
            CommentUpdateDTO dto = new CommentUpdateDTO();
            dto.setContent("Updated content");

            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(commentRepository.findById(1L)).thenReturn(Optional.of(mockComment));
            when(commentRepository.save(any(Comment.class))).thenAnswer(i -> i.getArguments()[0]);

            CommentDTO result = commentService.updateComment(1L, dto);

            assertEquals("Updated content", result.content());
            verify(commentRepository, times(1)).save(mockComment);
        }

        @Test
        void updateComment_shouldThrow_whenNotOwner() {
            CommentUpdateDTO dto = new CommentUpdateDTO();
            dto.setContent("Updated content");

            User otherUser = new User();
            otherUser.setId(2L);

            mockComment.setUser(otherUser);

            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(commentRepository.findById(1L)).thenReturn(Optional.of(mockComment));

            assertThrows(AccessDeniedException.class,
                    () -> commentService.updateComment(1L, dto));
        }

        @Test
        void updateComment_shouldThrow_whenCommentNotFound() {
            when(commentRepository.findById(1L)).thenReturn(Optional.empty());

            CommentUpdateDTO dto = new CommentUpdateDTO();
            dto.setContent("Test");

            assertThrows(ResourceNotFoundException.class,
                    () -> commentService.updateComment(1L, dto));
        }
    }

    // Delete tests

    @Nested
    class DeleteCommentTests {
        @Test
        void deleteComment_shouldDelete_whenOwner() {
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(commentRepository.findById(1L)).thenReturn(Optional.of(mockComment));

            commentService.deleteComment(1L);

            verify(commentRepository, times(1)).delete(mockComment);
        }

        @Test
        void deleteComment_shouldThrow_whenNotOwner() {
            User otherUser = new User();
            otherUser.setId(2L);
            mockComment.setUser(otherUser);

            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);
            when(commentRepository.findById(1L)).thenReturn(Optional.of(mockComment));

            assertThrows(AccessDeniedException.class,
                    () -> commentService.deleteComment(1L));

            verify(commentRepository, never()).delete(any());
        }

        @Test
        void deleteComment_shouldThrow_whenCommentNotFound() {
            when(commentRepository.findById(1L)).thenReturn(Optional.empty());
            when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);

            assertThrows(ResourceNotFoundException.class,
                    () -> commentService.deleteComment(1L));
        }
    }

}