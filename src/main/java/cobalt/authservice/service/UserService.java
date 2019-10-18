package cobalt.authservice.service;

import cobalt.authservice.dto.ForgotPasswordDto;
import cobalt.authservice.dto.SetNewPasswordDto;
import cobalt.authservice.dto.UserDto;
import cobalt.authservice.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User createUser(User user);

    Optional login(UserDto userDto);

    Optional forgotPassword(String url, ForgotPasswordDto forgotPasswordDto);

    Optional verifyResetURL(String resetToken);

    Optional resetPassword(String resetToken, SetNewPasswordDto setNewPasswordDto);
}
