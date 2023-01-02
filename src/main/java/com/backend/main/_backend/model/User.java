package com.backend.main._backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
@Builder
public class User {



    @Id
    private String id;
    private String email;
    private String username;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Post> posts = new HashSet<>();

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
    public Set<Post> getPosts() {
        return posts;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }


}
