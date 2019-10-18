package cobalt.authservice.persistence;

import cobalt.authservice.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindByEmail_thenReturnUser() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("test123");
        userRepository.save(user);

        User foundUser = userRepository.findUserByEmail("test@gmail.com");
        assertThat(user.getEmail()).isEqualTo(foundUser.getEmail());
    }
}
