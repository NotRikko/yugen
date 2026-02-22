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
import rikko.yugen.dto.post.PostDTO;
import rikko.yugen.exception.ResourceNotFoundException;
import rikko.yugen.helpers.CurrentUserHelper;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Post;
import rikko.yugen.model.User;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ProductRepository productRepository;

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



    //Get by id tests

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

    //Get by artist name tests

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
}