package com.example.securitystudy.post.service;


import com.example.securitystudy.post.entity.Post;
import com.example.securitystudy.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return postRepository.existsById(id);
    }
    

}
