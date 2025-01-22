package rikko.yugen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rikko.yugen.repository.PostRepository;
import rikko.yugen.repository.ArtistRepository;

import rikko.yugen.model.Post;
import rikko.yugen.model.Artist;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ArtistRepository artistRepository;

    public List<Post> getPostsByArtistId(Long artistId) {
        return postRepository.findByArtist_Id(artistId);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(PostCreateDTO postCreateDTO) {
        Post post = new Post();

        Artist artist = artistRepository.findById(postCreateDTO.getArtistId())
                                .orElseThrow(() -> new RuntimeException("Artist not found"));

        post.setLikes(null);
        post.setContent(postCreateDTO.getContent());
        post.setArtist(artist);
        post.setProduct(postCreateDTO.getProduct());    

        return postRepository.save(post);
    }
}
