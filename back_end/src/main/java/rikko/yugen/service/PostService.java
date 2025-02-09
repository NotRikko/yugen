package rikko.yugen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.ArtistRepository;
import rikko.yugen.repository.ProductRepository;

import rikko.yugen.model.Post;
import rikko.yugen.model.Product;
import rikko.yugen.dto.PostCreateDTO;
import rikko.yugen.model.Artist;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Post> getPostsByArtistId(Long artistId) {
        return postRepository.findByArtist_Id(artistId);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public Post createPost(PostCreateDTO postCreateDTO) {
        Artist artist = artistRepository.findById(postCreateDTO.getArtistId())
                                .orElseThrow(() -> new RuntimeException("Artist not found"));
        Product product = productRepository.findById(postCreateDTO.getProductId())
                                      .orElseThrow(() -> new RuntimeException("Product not found"));

        Post post = new Post();

        post.setContent(postCreateDTO.getContent());
        post.setArtist(artist);
        post.setProduct(product);    

        return postRepository.save(post);
    }
}
