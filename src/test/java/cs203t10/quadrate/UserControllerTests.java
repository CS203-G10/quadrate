package cs203t10.quadrate;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs203t10.quadrate.user.User;
import cs203t10.quadrate.user.UserController;
import cs203t10.quadrate.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createUser_Ok() throws Exception {
        User input = new User(null, "fangzhou", "password", "user");
        User output = new User(1L, "fangzhou", "password", "user");

        when(userService.createUser(any(User.class))).thenAnswer(i -> {
            User user = i.getArgument(0, User.class);
            user.setId(1L);
            return user;
        });

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(output)));
    }

    @Test
    void getAllUsers_Ok() throws Exception {
        List<User> users = List.of(
                new User("fangzhou"),
                new User("sin3142")
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(users)));
    }

    // TODO: Remainder of tests
}
