package com.backend.main._backend.controller;

import com.backend.main._backend.model.ImagePost;
import com.backend.main._backend.model.Post;
import com.backend.main._backend.service.PostService;
import com.backend.main._backend.service.UserService;
import com.backend.main._backend.util.ImageUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
@AllArgsConstructor
@RequestMapping("/image")
@CrossOrigin("http://localhost:3000/")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private  UserService userService;


    private final String FOLDER_PATH="C:/Users/zeink/source/repos/Arttok/post_images/";


    @PostMapping(value = "/fileSystem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createPost(@RequestParam ("file") MultipartFile file,
                                             @RequestParam ("description") String description,
                                             @AuthenticationPrincipal OAuth2User user) throws IOException {
        Post post = Post.builder()
                .description(description)
                .imagePath(uploadImageToFileSystem(file))
                .user(userService.getUserById(user.getAttribute("sub")))
                .build();
        Post postFromDb = postService.Create(post);
        if (postFromDb !=  null) {
            return new ResponseEntity<>("nieuwe post is gemaakt", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("niet gelukt om het post te maken", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/fileSystem/{postId}")
    public ResponseEntity<?> getPost(@PathVariable int postId) throws IOException {
        Post post = postService.findById(postId);
        byte[] image= downloadImageFromFileSystem(post.getImagePath());
        ImagePost imagePost = ImagePost.builder()
                .id(post.getId())
                .description(post.getDescription())
                .image(image)
                .user(post.getUser()).build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(imagePost);

    }
    @GetMapping(value = "/home")
    public ResponseEntity<?> GetAllPosts() throws IOException {

       List<Post> posts = postService.getAll();
       List<ImagePost> imagePosts = new ArrayList<>();
       for (Post post : posts){
           byte[] image= downloadImageFromFileSystem(post.getImagePath());
           ImagePost imagePost = ImagePost.builder()
                   .id(post.getId())
                   .description(post.getDescription())
                   .image(image)
                   .user(post.getUser()).build();
           imagePosts.add(imagePost);
       }
        return ResponseEntity.status(HttpStatus.OK)
                .body(imagePosts);

    }
    @GetMapping(value = "/profile/{username}")
    public ResponseEntity<?> GetUserPosts(@PathVariable("username") String username) throws IOException {

        List<Post> posts = postService.findByUser(username);
        List<ImagePost> imagePosts = new ArrayList<>();
        for (Post post : posts){
            byte[] image= downloadImageFromFileSystem(post.getImagePath());
            ImagePost imagePost = ImagePost.builder()
                    .id(post.getId())
                    .description(post.getDescription())
                    .image(image)
                    .user(post.getUser()).build();
            imagePosts.add(imagePost);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(imagePosts);

    }

    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath=FOLDER_PATH+file.getOriginalFilename();
        file.transferTo(new File(filePath));
            return filePath;
    }

    public byte[] downloadImageFromFileSystem(String filePath) throws IOException {
        byte[] image = Files.readAllBytes(new File(filePath).toPath());
        return image;
    }
}
