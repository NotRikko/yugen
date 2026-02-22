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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;
import rikko.yugen.dto.post.PostCreateDTO;
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.dto.post.PostUpdateDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Post;
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

    private Artist mockArtist;

    @BeforeEach
    void setUp() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("Rikko");
        mockUser.setDisplayName("Rikko");
        mockUser.setEmail("rikko@test.com");

        mockArtist = new Artist();
        mockArtist.setId(1L);
        mockArtist.setArtistName("Rikko");
        mockArtist.setBio("I am a test");

        mockArtist.setUser(mockUser);
        mockUser.setIsArtist(true);
        mockUser.setArtist(mockArtist);
    }



    // Get by id tests

    @Test
    void getPostById_shouldReturnPostDTO_whenPostExists() {
        Post post = new Post();
        post.setId(1L);
        post.setContent("Test content");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostDTO result = postService.getPostById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Test content", result.content());

        verify(postRepository).findById(1L);
    }

    @Test
    void getPostById_shouldThrowException_whenPostDoesNotExist() {
        when(postRepository.findById(9999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(9999L));
        verify(postRepository).findById(9999L);
    }

    // Get by artistId tests

    @Test
    void getPostsByArtistId_shouldReturnPaginatedPosts() {
        Post post1 = new Post();
        post1.setId(1L);
        post1.setContent("Test content");
        post1.setArtist(mockArtist);

        Post post2 = new Post();
        post2.setId(2L);
        post2.setContent("Test content2");
        post2.setArtist(mockArtist);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Post> postPage = new PageImpl<>(
                List.of(post1, post2),
                pageable,
                2
        );

        when(postRepository.findByArtist_Id(1L, pageable))
                .thenReturn(postPage);

        Page<PostDTO> result =
                postService.getPostsByArtistId(1L, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        assertEquals(1L, result.getContent().get(0).id());
        assertEquals("Test content", result.getContent().get(0).content());

        assertEquals(2L, result.getContent().get(1).id());
        assertEquals("Test content2", result.getContent().get(1).content());

        verify(postRepository).findByArtist_Id(1L, pageable);
    }

    // Get by artist name tests

    @Test
    void getPostsByArtistName_shouldReturnPaginatedPosts() {
        Pageable pageable = PageRequest.of(0, 2);

        Post post1 = new Post();
        post1.setContent("Post 1");
        post1.setArtist(mockArtist);
        Post post2 = new Post();
        post2.setContent("Post 2");
        post2.setArtist(mockArtist);

        Page<Post> postPage = new PageImpl<>(List.of(post1, post2), pageable, 2);
        when(postRepository.findByArtist_ArtistName("Rikko", pageable)).thenReturn(postPage);

        Page<PostDTO> result = postService.getPostsByArtistName("Rikko", pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        PostDTO first = result.getContent().get(0);
        assertEquals("Post 1", first.content());

        PostDTO second = result.getContent().get(1);
        assertEquals("Post 2", second.content());

        verify(postRepository).findByArtist_ArtistName("Rikko", pageable);
    }

    // Get all posts tests

    @Test
    void getAllPosts_shouldReturnPaginatedPosts() {
        Post post1 = new Post();
        post1.setContent("Post 1");
        post1.setArtist(mockArtist);
        Post post2 = new Post();
        post2.setContent("Post 2");
        post2.setArtist(mockArtist);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(List.of(post1, post2));
        when(postRepository.findAll(pageable)).thenReturn(postPage);
        Page<PostDTO> result = postService.getAllPosts(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        PostDTO first = result.getContent().get(0);
        assertEquals("Post 1", first.content());

        PostDTO second = result.getContent().get(1);
        assertEquals("Post 2", second.content());

        verify(postRepository).findAll(pageable);
    }

    // Create post tests

    @Test
    void createPost_shouldReturnPostDTO_whenPostCreated() {
        User mockUser = new User();
        mockUser.setId(1L);

        when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);

        Artist mockArtist = new Artist();
        mockArtist.setId(10L);
        mockArtist.setArtistName("Rikko");
        mockArtist.setUser(mockUser);

        when(artistRepository.findByUserId(1L))
                .thenReturn(Optional.of(mockArtist));

        PostCreateDTO dto = new PostCreateDTO();
        dto.setContent("Test content");

        Post savedPost = new Post();
        savedPost.setContent("Test content");
        savedPost.setArtist(mockArtist);

        when(postRepository.save(any(Post.class)))
                .thenReturn(savedPost);

        PostDTO result = postService.createPost(dto, null);

        assertNotNull(result);
        assertEquals("Test content", result.content());

        verify(currentUserHelper).getCurrentUser();
        verify(artistRepository).findByUserId(1L);
        verify(postRepository).save(any(Post.class));
    }

    // Update post tests

    @Test
    void updatePost_shouldUpdateContent_whenContentProvided() {
        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setContent("Old content");

        PostUpdateDTO dto = new PostUpdateDTO();
        dto.setContent("New content");

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));

        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PostDTO result = postService.updatePost(1L, dto);

        assertEquals("New content", existingPost.getContent());
        assertEquals("New content", result.content());

        verify(postRepository).findById(1L);
        verify(postRepository).save(existingPost);
    }

    @Test
    void updatePost_shouldThrowException_whenPostNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        PostUpdateDTO dto = new PostUpdateDTO();
        dto.setContent("New content");

        assertThrows(ResourceNotFoundException.class, () -> postService.updatePost(1L, dto));

        verify(postRepository).findById(1L);
        verify(postRepository, never()).save(any());
    }

    @Test
    void updatePost_shouldNotSave_whenContentIsNull() {
        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setContent("Old content");

        PostUpdateDTO dto = new PostUpdateDTO();

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));

        PostDTO result = postService.updatePost(1L, dto);

        assertEquals("Old content", result.content());
        verify(postRepository, never()).save(any());
    }

    @Test
    void deletePost_shouldCallRepositoryDelete_whenUserOwnsPost() {
        User mockUser = new User();
        mockUser.setId(1L);
        when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);

        Artist mockArtist = new Artist();
        mockArtist.setUser(mockUser);

        Post mockPost = new Post();
        mockPost.setArtist(mockArtist);

        when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));

        postService.deletePost(1L);
        verify(postRepository).delete(mockPost);
    }

    @Test
    void deletePost_shouldThrowAccessDeniedException_whenUserDoesNotOwnPost() {
        User mockUser = new User();
        mockUser.setId(1L);
        when(currentUserHelper.getCurrentUser()).thenReturn(mockUser);

        User otherUser = new User();
        otherUser.setId(2L);

        Artist mockArtist = new Artist();
        mockArtist.setUser(otherUser);

        Post mockPost = new Post();
        mockPost.setArtist(mockArtist);

        when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));

        assertThrows(AccessDeniedException.class, () -> postService.deletePost(1L));
        verify(postRepository, never()).delete(any());
    }

    @Test
    void uploadAndSaveFiles_shouldUploadAndCreateImages() throws Exception {
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