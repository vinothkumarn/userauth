package cobalt.authservice.controller;

import cobalt.authservice.dto.SetNewPasswordDto;
import cobalt.authservice.dto.UserDto;
import cobalt.authservice.entity.AuthToken;
import cobalt.authservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void givenCorrectLoginDetails_thenShouldReturnAuthToken() throws Exception {
        UserDto userDto = UserDto.builder()
                .email("test@gmail.com")
                .password("test123")
                .build();

        AuthToken authToken = new AuthToken();
        String generatedToken = UUID.randomUUID().toString();
        authToken.setAuthtoken(generatedToken);

        given(userService.login(userDto)).willReturn(Optional.of(authToken));
        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authtoken", is(generatedToken)));
    }

    @Test
    public void givenCorrectResetToken_thenShouldSuccessMessage() throws Exception {

        SetNewPasswordDto setNewPasswordDto = SetNewPasswordDto.builder()
                .newpassword("newpass")
                .build();

        String resetUrl = UUID.randomUUID().toString();
        given(userService.resetPassword(resetUrl, setNewPasswordDto))
                .willReturn(Optional.of("Password reset successfully"));
        mvc.perform(post("/resetPassword/" + resetUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(setNewPasswordDto)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("\"Password reset successfully\""));
    }
}
