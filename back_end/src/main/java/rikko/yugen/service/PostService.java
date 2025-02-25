package rikko.yugen.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.ProductRepository;
import rikko.yugen.repository.LikeRepository;

import rikko.yugen.model.Post;
import rikko.yugen.model.Product;
import rikko.yugen.dto.PostCreateDTO;
import rikko.yugen.model.Artist;
import rikko.yugen.model.Like;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LikeRepository likeRepository;

    public List<Post> getPostsByArtistId(Long artistId) {
        return postRepository.findByArtist_Id(artistId);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Set<Like> getPostLikes(Long postId) {
        return likeRepository.findByContentIdAndContentType(postId, "POST");
    }

    @Transactional
    public Post createPost(PostCreateDTO postCreateDTO) {
        Artist artist = artistRepository.findById(postCreateDTO.getArtistId())
                                .orElseThrow(() -> new RuntimeException("Artist not found"));
                                
        Product product = (postCreateDTO.getProductId() != null)
        ? productRepository.findById(postCreateDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"))
        : null;
                            
        Post post = new Post();

        post.setContent(postCreateDTO.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setArtist(artist);
        post.setProduct(product);    

        return postRepository.save(post);
    }
}
