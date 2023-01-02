package com.backend.main._backend.service;

import com.backend.main._backend.model.Post;
import com.backend.main._backend.model.User;
import com.backend.main._backend.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepo userRepo;
    UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepo);
    }

    @Test
    void saveUser() {
        //given
        User user = new User();

        //when
        userService.saveUser(user);
        //then
        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepo).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertSame(capturedUser, user);
    }

    @Test
    void getUserById() {
        //given
        String userId = "Id";
        //when
        userService.getUserById(userId);
        //then
        verify(userRepo).findById(userId);
    }

    @Test
    void getUserByemail() {
        //given
        String email = "email";
        User user = new User();
        //when
        given(userRepo.findByemail(email)).willReturn(user);
        User result = userService.getUserByemail(email);
        //then
        verify(userRepo).findByemail(email);
        assertSame(user, result);
    }

    @Test
    void ShouldReturnNullIfUserIsNull() {
        //given
        String email = "email";
        //when
        User result = userService.getUserByemail(email);
        //then
        assertNull(result);
    }
}