package cobalt.authservice.persistence;

import cobalt.authservice.entity.ResetToken;
import cobalt.authservice.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    private User user;

    private ResetToken resetToken;

    private UUID randomToken;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("test123");

        resetToken = new ResetToken();
        randomToken = UUID.randomUUID();
        resetToken.setUser(user);
        resetToken.setEmailresettoken(randomToken.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, 60);
        resetToken.setExpiryDate(new Date(calendar.getTime().getTime()));

        userRepository.save(user);
        resetTokenRepository.save(resetToken);
    }

    @Test
    public void whenFindByEmail_thenReturnUser() {

        User foundUser = userRepository.findUserByEmail("test@gmail.com");
        assertThat(user.getEmail()).isEqualTo(foundUser.getEmail());
    }

    @Test
    public void whenFindByResetToken_thenContainUserInObject() {
        ResetToken foundresetToken = resetTokenRepository.findPasswordResetToken(randomToken.toString());
        assertThat(user).isEqualTo(foundresetToken.getUser());
    }
}
