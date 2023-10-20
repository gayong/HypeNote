package com.example.securitystudy.post.controller;


import com.example.securitystudy.post.dto.CreatePostDto;
import com.example.securitystudy.post.entity.Post;
import com.example.securitystudy.post.repository.PostRepository;
import com.example.securitystudy.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;
    private final PostService postService;

    @Autowired
    public PostController(PostRepository postRepository, PostService postService) {
        this.postRepository = postRepository;
        this.postService = postService;
    }

    @Tag(name = "게시글", description = "게시글")
    @PostMapping("/create")
    @Operation(summary = "게시글 생성")
    public Post createPost(@RequestBody CreatePostDto newPost) {
        Post post = Post.builder()
                .title(newPost.getTitle())
                .content(newPost.getContent())
                .build();

        return postRepository.save(post);
    }

    @Tag(name = "게시글", description = "게시글")
    @GetMapping("")
    @Operation(summary = "전체 게시글 정보 조회")
    public List<Post> readAllPosts() {
        return postRepository.findAll();
    }


    @Tag(name = "게시글", description = "게시글")
    @GetMapping("/{id}")
    @Operation(summary = "단일 게시글 정보 조회")
    public Optional<Post> readPost(@PathVariable Long id) {
        return postRepository.findById(id);
    }


    @Tag(name = "게시글", description = "게시글")
    @PutMapping("/update/{id}")
    @Operation(summary = "게시글 수정")
    public Optional<Post> updatePost(@PathVariable Long id,
                                     @RequestBody CreatePostDto updatedPost) {
        return postRepository.findById(id).map(post -> {
            if(updatedPost.getTitle() != null)
                post.setTitle(updatedPost.getTitle());
            if(updatedPost.getContent() != null)
                post.setContent(updatedPost.getContent());
            return this.postService.save(post);
        });
    }

    @Tag(name = "게시글", description = "게시글")
    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제")
    public void deleteNote(@PathVariable Long id) {
        if (postService.existsById(id)) {
            this.postService.deleteById(id);
        }
    }

}
