package com.danielturato.recipe.user;

import com.danielturato.recipe.core.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTests extends BaseTest {

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() throws Exception {
        user = userBuilder();
    }

    @Test
    public void findUserByUsername() throws Exception {
        when(userRepository.findByUsername("daniel")).thenReturn(user);
        userService.findByUsername("daniel");
        verify(userRepository).findByUsername(any(String.class));
    }

    @Test
    public void saveAUser() throws Exception {
        doAnswer(invocation -> null).when(userRepository).save(user);
        userService.save(user);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void findAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(new ArrayList<User>());
        userService.findAll();
        verify(userRepository).findAll();
    }
}
