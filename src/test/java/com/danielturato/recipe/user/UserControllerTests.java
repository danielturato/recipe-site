package com.danielturato.recipe.user;

import com.danielturato.recipe.core.BaseControllerTest;
import com.danielturato.recipe.core.BaseTest;
import com.danielturato.recipe.core.WithMockCustomUser;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTests extends BaseControllerTest {

    @MockBean
    UserServiceImpl userService;

    /**
     * Anyone can visit login page
     */
    @Test
    public void loginPageLoads() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    /**
     * Login is successful
     */
    @Test
    public void canLoginWithUser() throws Exception {
        mockMvc.perform(formLogin("/login")
                    .user("daniel").password("password"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(forwardedUrl("/recipes"))
                .andExpect(authenticated());
    }

    /**
     * Login is not successful
     */
    @Test
    public void loginFailsWithUnknownUser() throws Exception {
        mockMvc.perform(formLogin("login")
                    .user("test").password("pass")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(unauthenticated());
    }

    /**
     * Logout is successful
     */
    @Test
    public void logoutWorks() throws Exception {
        mockMvc.perform(logout("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login"));
    }

    /**
     * Sign-up page loads
     */
    @Test
    public void getSignUpPageLoads() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"));
    }

    /**
     * Signing up a new user works
     */
    @Test
    public void signUpNewUser() throws Exception {
        when(userService.findByUsername(any(String.class))).thenReturn(null);
        doAnswer(invocation -> null).when(userService).save(any(User.class));

        mockMvc.perform(post("/sign-up")
                    .param("username", "testUser")
                    .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("flash"))
                .andExpect(redirectedUrl("/login"));

        verify(userService).findByUsername(any(String.class));
        verify(userService).save(any(User.class));
    }

    /**
     * Signing up a user with username already taken doesn't work
     */
    @Test
    public void signUpUserWithUsernameAlreadyTakenDoesntWork() throws Exception {
        User user = userBuilder();
        when(userService.findByUsername(any(String.class))).thenReturn(user);

        mockMvc.perform(post("/sign-up")
                .param("username", "daniel")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("flash"))
                .andExpect(redirectedUrl("/sign-up"));

        verify(userService).findByUsername(any(String.class));
    }

    @Test
    @WithMockCustomUser(username = "daniel")
    public void viewUserDetailPage() throws Exception {
        User user = userBuilder();
        when(userService.findByUsername(any(String.class))).thenReturn(user);

        mockMvc.perform(get("/users/daniel"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user", "favs"))
                .andExpect(authenticated());

        verify(userService, times(2)).findByUsername(any(String.class));
    }
}
