package com.backend.main._backend.repository;

import com.backend.main._backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post,Integer> {

     Post findById (int postId);
     List<Post> findAllByUser_Username(String username);
}
