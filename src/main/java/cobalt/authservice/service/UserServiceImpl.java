package cobalt.authservice.service;

import cobalt.authservice.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Override
    public User createUser(User user) {
        return null;
    }
}