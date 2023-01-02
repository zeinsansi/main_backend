package com.backend.main._backend.controller;

import com.backend.main._backend.model.Post;
import com.backend.main._backend.model.User;
import com.backend.main._backend.service.PostService;
import com.backend.main._backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;
    @Mock
    private UserService userService;

    private PostController postController;

    @BeforeEach
    void setUp() {
        postController = new PostController(postService, userService);
    }

    @Test
    @DisplayName("Should return 400 when the post is not created")
    void createPostWhenPostIsNotCreatedThenReturn400() throws IOException {
        //given
        MultipartFile file = mock(MultipartFile.class);
        String description = "description";
        OAuth2User user = mock(OAuth2User.class);
        //when
        when(user.getAttribute("sub")).thenReturn("id");
        when(userService.getUserById("id")).thenReturn(User.builder().build());
        when(postService.Create(any())).thenReturn(null);
        ResponseEntity<Object> responseEntity = postController.createPost(file, description, user);
        //then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should return 201 when the post is created")
    void createPostWhenPostIsCreatedThenReturn201() throws IOException {
        //given
        MultipartFile file = mock(MultipartFile.class);
        String description = "description";
        OAuth2User user = mock(OAuth2User.class);
        Post post =
                Post.builder()
                        .description(description)
                        .imagePath("imagePath")
                        .user(User.builder().id(user.getAttribute("sub")).build())
                        .build();
        //when
        given(postService.Create(any())).willReturn(post);
        ResponseEntity<Object> responseEntity = postController.createPost(file, description, user);
        //then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should return 201 when getting the post ")
    void getPost() throws IOException {
        //given
        int postId = 20;
        User user = new User();
        Post post =
                Post.builder()
                        .description("description")
                        .imagePath("C:/Users/zeink/source/repos/Arttok/post_images/thumbnail_Arttok logo (1).png")
                        .user(user)
                        .id(20).build();
        //when
        given(postService.findById(postId)).willReturn(post);
        ResponseEntity<?> responseEntity = postController.getPost(postId);
        //then
        assertSame(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should return 201 when getting all posts ")
    void getAllPosts() throws IOException {
        //given
        User user = new User();
        List<Post> posts = new ArrayList<>();
        Post post = Post.builder().description("description").imagePath("C:/Users/zeink/source/repos/Arttok/post_images/thumbnail_Arttok logo (1).png").user(user).id(20).build();
        Post post2 = Post.builder().description("description").imagePath("C:/Users/zeink/source/repos/Arttok/post_images/thumbnail_Arttok logo (1).png").user(user).id(21).build();
        posts.add(post);
        posts.add(post2);
        //when
        given(postService.getAll()).willReturn(posts);
        ResponseEntity<?> responseEntity = postController.GetAllPosts();
        //then
        assertSame(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should return 201 when getting a user posts ")
    void getUserPosts() throws IOException {
        //given
        String username = "Zein";
        User user = new User();
        List<Post> posts = new ArrayList<>();
        Post post = Post.builder().description("description").imagePath("C:/Users/zeink/source/repos/Arttok/post_images/thumbnail_Arttok logo (1).png").user(user).id(20).build();
        Post post2 = Post.builder().description("description").imagePath("C:/Users/zeink/source/repos/Arttok/post_images/thumbnail_Arttok logo (1).png").user(user).id(21).build();
        posts.add(post);
        posts.add(post2);
        //when
        given(postService.findByUser(username)).willReturn(posts);
        ResponseEntity<?> responseEntity = postController.GetUserPosts(username);
        //then
        assertSame(HttpStatus.OK, responseEntity.getStatusCode());
    }
}