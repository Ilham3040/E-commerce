package com.example.shoppingapi.controller;

import com.example.shoppingapi.DotenvLoader;
import com.example.shoppingapi.dto.create.UserCreateDTO;
import com.example.shoppingapi.model.User;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.UserRepository;
import com.example.shoppingapi.service.UserService;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public static void setUp() {
        DotenvLoader.load();
    }

    private final ModelHelper<User> helper = ModelHelperFactory.getModelHelper(User.class);

    private User createUser() throws Exception {
        User newuser = helper.createModel(1);
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername(newuser.getUsername());
        userCreateDTO.setEmail(newuser.getEmail());
        userCreateDTO.setPhoneNumber(newuser.getPhoneNumber());
        return userService.createUser(userCreateDTO);
    }

    private String createUserJson(String username, String email, String phoneNumber) {
        return String.format("{ \"username\": \"%s\", \"email\": \"%s\", \"phoneNumber\": \"%s\" }",
                username, email, phoneNumber);
    }

    private void assertUserResponse(Long userId, String username, String email, String phoneNumber) throws Exception {
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fetched user"))
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testCreateUser() throws Exception {
        String jsonContent = createUserJson("testuser", "testuser@example.com", "1234567890");

        // Perform the POST request to create the user
        String responseContent = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created user"))
                .andReturn().getResponse().getContentAsString();

        // Extract the created user's ID from the response
        Integer createdUserIdInteger = JsonPath.parse(responseContent).read("$.data.userId");

        // Convert the Integer to Long
        Long createdUserId = Long.valueOf(createdUserIdInteger.toString());

        // Retrieve and validate the created user using the ID from the response
        User createdUser = userService.getUserById(createdUserId);
        assertEquals("testuser", createdUser.getUsername());
    }


    @Test
    @Transactional
    @Rollback(true)
    public void testGetAllUsers() throws Exception {
        User createdUser = createUser();

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fetched all users"))
                .andExpect(jsonPath("$.data[0].userId").value(createdUser.getUserId()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetUserById() throws Exception {
        User createdUser = createUser();

        assertUserResponse(createdUser.getUserId(), createdUser.getUsername(),
                createdUser.getEmail(), createdUser.getPhoneNumber());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testUpdateUser() throws Exception {
        User createdUser = createUser();

        String jsonContent = createUserJson("updateduser", "updateduser@example.com", "0987654321");

        mockMvc.perform(put("/api/users/{id}", createdUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated"))
                .andExpect(jsonPath("$.data.userId").value(createdUser.getUserId()));

        assertUserResponse(createdUser.getUserId(), "updateduser", "updateduser@example.com", "0987654321");
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testPartialUpdateUser() throws Exception {
        User createdUser = createUser();

        String jsonContent = "{ \"email\": \"newemail@example.com\" }";

        mockMvc.perform(patch("/api/users/{id}", createdUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User partially updated"))
                .andExpect(jsonPath("$.data.userId").value(createdUser.getUserId()));

        assertUserResponse(createdUser.getUserId(), createdUser.getUsername(),
                "newemail@example.com", createdUser.getPhoneNumber());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testDeleteUser() throws Exception {
        User createdUser = createUser();

        mockMvc.perform(delete("/api/users/{id}", createdUser.getUserId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("User deleted"));

        assertFalse(userRepository.existsById(createdUser.getUserId()));
    }
}
