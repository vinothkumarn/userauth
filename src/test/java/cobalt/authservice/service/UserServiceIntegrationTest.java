package cobalt.authservice.service;

import cobalt.authservice.TestConfiguration;
import cobalt.authservice.dto.SetNewPasswordDto;
import cobalt.authservice.dto.UserDto;
import cobalt.authservice.entity.AuthToken;
import cobalt.authservice.entity.ResetToken;
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

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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

    private ResetToken resetToken;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("test123");

        resetToken = new ResetToken();
        UUID randomToken = UUID.randomUUID();
        resetToken.setUser(user);
        resetToken.setEmailresettoken(randomToken.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, 60);
        resetToken.setExpiryDate(new Date(calendar.getTime().getTime()));

        Mockito.when(resetTokenRepository.findPasswordResetToken(randomToken.toString()))
                .thenReturn(resetToken);

        Mockito.when(userRepository.save(user))
                .thenReturn(user);
        Mockito.doNothing().when(resetTokenRepository).delete(resetToken);
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

    @Test
    public void whenProvideCorrectResetToken_thenSetNewPassword() {

        SetNewPasswordDto setNewPasswordDto = SetNewPasswordDto.builder()
                .newpassword("newpassword")
                .build();
        Optional message = userService.resetPassword(resetToken.getEmailresettoken(), setNewPasswordDto);

        assertThat(Optional.of("Password reset successfully"))
                .isEqualTo(message);
    }

    @Test
    public void whenProvideInvalidResetToken_thenShouldThrowError() {

        SetNewPasswordDto setNewPasswordDto = SetNewPasswordDto.builder()
                .newpassword("newpassword")
                .build();
        Optional message = userService.resetPassword("invalidtoken-12323232", setNewPasswordDto);

        assertThat(Optional.of("Invalid reset token"))
                .isEqualTo(message);
    }

}


