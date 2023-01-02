package com.backend.main._backend.service;

import com.backend.main._backend.model.Post;
import com.backend.main._backend.repository.PostRepo;
import com.backend.main._backend.util.ImageUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PostService {

    @Autowired
    private PostRepo postRepo;

    public Post Create(Post post){
        if(post != null){
            Post postFromDb = postRepo.save(post);
            return postFromDb;
        }
        return null;
    }

    public Post findById(int postId){
        if(postId != 0){
            Post post = postRepo.findById(postId);
            return post;
        }
        return null;
    }

    public List<Post> getAll() throws IOException{
        List<Post> posts = postRepo.findAll();
        return posts;
    }
    public List<Post> findByUser(String username){
        if(username != null){
            List<Post> posts = postRepo.findAllByUser_Username(username);
            return posts;
        }
        return null;
    }

}
