package cobalt.authservice.service;

import cobalt.authservice.TestConfiguration;
import cobalt.authservice.dto.UserDto;
import cobalt.authservice.entity.AuthToken;
import cobalt.authservice.entity.User;
import cobalt.authservice.persistence.AuthTokenRepository;
import cobalt.authservice.persistence.ResetTokenRepository;
import cobalt.authservice.persistence.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthTokenRepository authTokenRepository;

    @MockBean
    private ResetTokenRepository resetTokenRepository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("test123");

        Mockito.when(userRepository.findUserByEmail(user.getEmail()))
                .thenReturn(user);
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
    }

    @Test
    public void whenInvalidPassword_thenLoginShouldFail() {

        UserDto userDto = UserDto.builder()
                .email("test@gmail.com")
                .password("invalidPassword")
                .build();

        Optional message = userService.login(userDto);
        assertThat(message)
                .isEqualTo(Optional.of("Login failed"));
    }

    @Test
    public void testCreateUser() {
        assertThat("test@gmail.com")
                .isEqualTo(userService.createUser(user).getEmail());
    }

}


