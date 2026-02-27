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
import org.springframework.web.multipart.MultipartFile;
import rikko.yugen.dto.post.PostCreateDTO;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.dto.post.PostUpdateDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Post;
import rikko.yugen.model.Role;
import rikko.yugen.model.User;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.PostRepository;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ImageService imageService;

    @Mock
    private CurrentUserHelper currentUserHelper;

    @InjectMocks
    private PostService postService;

    //Mock artist

    private User user;
    private Artist artist;
    private Post post1;
    private Post post2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("Rikko");
        user.setIsArtist(true);
        user.setRole(Role.ARTIST);

        artist = new Artist();
        artist.setId(1L);
        artist.setArtistName("Rikko");
        artist.setUser(user);
        user.setArtist(artist);

        post1 = new Post();
        post1.setId(1L);
        post1.setContent("Post 1");
        post1.setArtist(artist);


        post2 = new Post();
        post2.setId(2L);
        post2.setContent("Post 2");
        post2.setArtist(artist);
    }

    // Test helpers
    private PostCreateDTO createPostCreateDTO(String content) {
        PostCreateDTO dto = new PostCreateDTO();
        dto.setContent(content);
        return dto;
    }

    private PostUpdateDTO createPostUpdateDTO(String content) {
        PostUpdateDTO dto = new PostUpdateDTO();
        dto.setContent(content);
        return dto;
    }


    // Get by id tests

    @Nested
    class GetPostsTests {

        @Test
        void getPostById_shouldReturnPostDTO_whenExists() {
            when(postRepository.findById(1L)).thenReturn(Optional.of(post1));

            PostDTO result = postService.getPostById(1L);

            assertEquals(1L, result.id());
            assertEquals("Post 1", result.content());
            verify(postRepository).findById(1L);
        }

        @Test
        void getPostById_shouldThrow_whenNotFound() {
            when(postRepository.findById(999L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(999L));
        }

        @Test
        void getPostsByArtistId_shouldReturnPage() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Post> page = new PageImpl<>(List.of(post1, post2), pageable, 2);
            when(postRepository.findByArtist_Id(artist.getId(), pageable)).thenReturn(page);

            Page<PostDTO> result = postService.getPostsByArtistId(artist.getId(), pageable);

            assertEquals(2, result.getTotalElements());
            assertEquals("Post 1", result.getContent().get(0).content());
            assertEquals("Post 2", result.getContent().get(1).content());
        }

        @Test
        void getPostsByArtistName_shouldReturnPage() {
            Pageable pageable = PageRequest.of(0, 2);
            Page<Post> page = new PageImpl<>(List.of(post1, post2), pageable, 2);
            when(postRepository.findByArtist_ArtistName("Rikko", pageable)).thenReturn(page);

            Page<PostDTO> result = postService.getPostsByArtistName("Rikko", pageable);

            assertEquals(2, result.getContent().size());
            assertEquals("Post 1", result.getContent().get(0).content());
            assertEquals("Post 2", result.getContent().get(1).content());
        }

        @Test
        void getAllPosts_shouldReturnPage() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Post> page = new PageImpl<>(List.of(post1, post2));
            when(postRepository.findAll(pageable)).thenReturn(page);

            Page<PostDTO> result = postService.getAllPosts(pageable);

            assertEquals(2, result.getContent().size());
        }
    }

    // Create post tests

    @Nested
    class CreatePostTests {

        @Test
        void createPost_shouldReturnPostDTO() {
            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(artistRepository.findByUserId(user.getId())).thenReturn(Optional.of(artist));

            PostCreateDTO dto = createPostCreateDTO("New Post");

            Post savedPost = new Post();
            savedPost.setContent(dto.getContent());
            savedPost.setArtist(artist);

            when(postRepository.save(any(Post.class))).thenReturn(savedPost);

            PostDTO result = postService.createPost(dto, null);

            assertEquals("New Post", result.content());
            verify(postRepository).save(any(Post.class));
        }
    }

    // Update post tests

    @Nested
    class UpdatePostTests {

        @Test
        void updatePost_shouldUpdateContentAndSave() {
            Post existingPost = post1;

            PostUpdateDTO dto = createPostUpdateDTO("New");

            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(artistRepository.findByUserId(user.getId())).thenReturn(Optional.of(artist));
            when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));
            when(postRepository.save(any(Post.class))).thenAnswer(i -> i.getArgument(0));

            PostDTO result = postService.updatePost(1L, dto);

            verify(postRepository).save(any(Post.class));
        }

        @Test
        void updatePost_shouldThrowAccessDenied_whenArtistIsNotOwner() {
            Post existingPost = post1;

            User otherUser = new User();
            otherUser.setRole(Role.ARTIST);

            Artist otherArtist = new Artist();
            otherArtist.setId(999L);
            otherUser.setArtist(otherArtist);

            when(currentUserHelper.getCurrentUser()).thenReturn(otherUser);
            when(artistRepository.findByUserId(otherUser.getId())).thenReturn(Optional.of(otherArtist));
            when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));

            assertThrows(AccessDeniedException.class, () ->
                    postService.updatePost(1L, createPostUpdateDTO("New"))
            );

            verify(postRepository, never()).save(any());
        }

        @Test
        void updatePost_shouldThrowResourceNotFound_whenNotFound() {
            when(postRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> postService.updatePost(1L, createPostUpdateDTO("New")));
        }

        @Test
        void updatePost_shouldNotSave_whenContentNullOrBlank() {
            Post existingPost = post1;

            user.setRole(Role.ARTIST);

            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));
            when(artistRepository.findByUserId(user.getId())).thenReturn(Optional.of(artist));

            PostDTO resultNull = postService.updatePost(1L, new PostUpdateDTO());
            assertEquals("Post 1", resultNull.content());

            PostUpdateDTO dtoBlank = new PostUpdateDTO();
            dtoBlank.setContent("   ");
            PostDTO resultBlank = postService.updatePost(1L, dtoBlank);
            assertEquals("Post 1", resultBlank.content());

            verify(postRepository, never()).save(any());
        }
    }

    // Delete tests

    @Nested
    class DeletePostTests {

        @Test
        void deletePost_shouldDelete_whenOwner() {
            when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
            when(currentUserHelper.getCurrentUser()).thenReturn(user);
            when(artistRepository.findByUserId(user.getId())).thenReturn(Optional.of(artist));

            postService.deletePost(1L);

            verify(postRepository).delete(post1);
        }

        @Test
        void deletePost_shouldThrow_whenNotOwner() {
            User otherUser = new User();
            otherUser.setId(2L);
            Artist otherArtist = new Artist();
            otherArtist.setUser(otherUser);

            when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
            when(currentUserHelper.getCurrentUser()).thenReturn(otherUser);
            when(artistRepository.findByUserId(otherUser.getId())).thenReturn(Optional.of(otherArtist));

            assertThrows(AccessDeniedException.class, () -> postService.deletePost(1L));
            verify(postRepository, never()).delete(any());
        }
    }

    // Upload files tests

    @Nested
    class UploadFilesTests {

        @Test
        void uploadAndSaveFiles_shouldCallCloudinaryAndImageService() throws Exception {
            MultipartFile file1 = mock(MultipartFile.class);
            MultipartFile file2 = mock(MultipartFile.class);
            List<MultipartFile> files = List.of(file1, file2);

            Post mockPost = new Post();

            when(cloudinaryService.uploadImage(file1)).thenReturn("url1");
            when(cloudinaryService.uploadImage(file2)).thenReturn("url2");

            Method method = PostService.class.getDeclaredMethod("uploadAndSaveFiles", List.class, Post.class);
            method.setAccessible(true);
            method.invoke(postService, files, mockPost);

            verify(cloudinaryService).uploadImage(file1);
            verify(cloudinaryService).uploadImage(file2);
            verify(imageService).createImageForPost("url1", mockPost);
            verify(imageService).createImageForPost("url2", mockPost);
        }
    }
}