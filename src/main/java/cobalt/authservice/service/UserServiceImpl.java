package cobalt.authservice.service;

import cobalt.authservice.dto.ForgotPasswordDto;
import cobalt.authservice.dto.SetNewPasswordDto;
import cobalt.authservice.dto.UserDto;
import cobalt.authservice.entity.AuthToken;
import cobalt.authservice.entity.ResetToken;
import cobalt.authservice.entity.User;
import cobalt.authservice.persistence.AuthTokenRepository;
import cobalt.authservice.persistence.ResetTokenRepository;
import cobalt.authservice.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AuthTokenRepository authTokenRepository;

    private final ResetTokenRepository resetTokenRepository;

    @Override
    public User createUser(User user) {
        // Email validation needed before saving the user
        User savedUser = userRepository.save(user);
        if (savedUser != null) {
            return savedUser;
        } else {
            throw new IllegalArgumentException("Invalid input data");
        }
    }

    @Override
    public Optional login(UserDto userDto) {
        User loggedInUser = userRepository.findUserByEmail(userDto.getEmail());
        if (BCrypt.checkpw(userDto.getPassword(), loggedInUser.getPassword())) {
            AuthToken authToken = new AuthToken();
            authToken.setUser(loggedInUser);
            authToken.setAuthtoken(UUID.randomUUID().toString());
            return Optional.of(authTokenRepository.save(authToken));
        } else {
            return Optional.of("Login failed");
        }
    }

    @Override
    public Optional forgotPassword(String url, ForgotPasswordDto forgotPasswordDto) {
        User loggedInUser = userRepository.findUserByEmail(forgotPasswordDto.getEmail());
        if (loggedInUser != null) {
            UUID randomToken = UUID.randomUUID();
            ResetToken resetToken = new ResetToken();
            resetToken.setUser(loggedInUser);
            resetToken.setEmailresettoken(randomToken.toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(new Date().getTime());
            // Setting one hour expiry time for reset url
            calendar.add(Calendar.MINUTE, 60);
            resetToken.setExpiryDate(new Date(calendar.getTime().getTime()));
            resetTokenRepository.save(resetToken);
            System.out.println(resetToken.getExpiryDate());
            // URL shouldn't be hardcoded. Added to simplify the task
            return Optional.of("http://localhost:8080/resetPassword/" + randomToken);
        } else {
            return Optional.of("Invalid email address");
        }
    }

    @Override
    public Optional verifyResetURL(String resetTokenUuid) {
        ResetToken resetToken = resetTokenRepository.findPasswordResetToken(resetTokenUuid);
        if (resetToken != null) {
            Calendar calendar = Calendar.getInstance();
            if (resetToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0) {
                return Optional.of("Expired reset token");
            }
            return Optional.of("Valid password reset token");
        }
        return Optional.of("Invalid reset token");
    }

    @Override
    public Optional resetPassword(String resetTokenUuid, SetNewPasswordDto setNewPasswordDto) {
        ResetToken resetToken = resetTokenRepository.findPasswordResetToken(resetTokenUuid);
        if (resetToken != null) {
            Calendar calendar = Calendar.getInstance();
            if (resetToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0) {
                return Optional.of("Expired reset token");
            }
            User user = resetToken.getUser();
            user.setPassword(setNewPasswordDto.getNewpassword());
            userRepository.save(user);
            resetTokenRepository.delete(resetToken);
            return Optional.of("Password reset successfully");
        }
        return Optional.of("Invalid reset token");
    }
}
