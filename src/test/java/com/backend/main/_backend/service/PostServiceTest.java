package com.backend.main._backend.service;

import com.backend.main._backend.model.Post;
import com.backend.main._backend.model.User;
import com.backend.main._backend.repository.PostRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepo postRepo;
    private PostService postService;

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepo);
    }

    @Test
    @DisplayName("Should verify that save was used and capturedPost is equal to post")
    void shouldCreatePostWhenNotNull() {
        //given
        User user = new User();
        Post post = new Post(
                15,
                "test description",
                "test imagePath",
                user
        );
        //when
        postService.Create(post);
        //then
        ArgumentCaptor<Post> postArgumentCaptor =
                ArgumentCaptor.forClass(Post.class);

        verify(postRepo).save(postArgumentCaptor.capture());

        Post capturedPost = postArgumentCaptor.getValue();

        assertSame(capturedPost, post);
    }

    @Test
    @DisplayName("Should verify that save was not used and return null ")
    void shouldReturnNullWhenPostIsNull() {
        //given
        Post post = null;
        //when
        Post createdPost = postService.Create(post);
        //then
        verify(postRepo, never()).save(post);
        assertNull(createdPost);
    }


    @Test
    @DisplayName("Should verify that findById was used with the same postId given")
    void ShouldGetPostByPostId() {
        //given
        int postId = 20;
        //when
        postService.findById(postId);
        //then
        verify(postRepo).findById(postId);
    }

    @Test
    @DisplayName("Should verify that findAll was used")
    void shouldGetAllPosts() throws IOException {
        //when
        postService.getAll();
        //then
        verify(postRepo).findAll();
    }

    @Test
    @DisplayName("Should verify that findAllByUser_Username was used and that capturedUsername is equal to username")
    void ShouldGetPostsByUsername() {
        //given
        String username = "Zein";
        //when
        postService.findByUser(username);
        //then
        ArgumentCaptor<String> userNameArgumentCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(postRepo).findAllByUser_Username(userNameArgumentCaptor.capture());

        String capturedUsername = userNameArgumentCaptor.getValue();

        assertSame(capturedUsername, username);
    }
}