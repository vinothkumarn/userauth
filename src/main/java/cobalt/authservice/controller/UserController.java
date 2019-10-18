package cobalt.authservice.controller;

import cobalt.authservice.dto.ForgotPasswordDto;
import cobalt.authservice.dto.SetNewPasswordDto;
import cobalt.authservice.dto.UserMapper;
import cobalt.authservice.entity.User;
import cobalt.authservice.dto.UserDto;
import cobalt.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public String getAllUsers() {
        return "test";
    }

    @PostMapping("/user")
    public Optional createUser(@Valid @RequestBody UserDto userDto) {
        try {
            User user = UserMapper.DtoToUser(userDto);
            return Optional.of(UserMapper.userToDto(userService.createUser(user)));
        } catch (Exception e) {
            return Optional.of("User creation failed");
        }
    }

    @PostMapping("/login")
    public Optional login(@Valid @RequestBody UserDto userDto) {

        return userService.login(userDto);
    }

    @PostMapping("/forgotPassword")
    public Optional forgotPassword(HttpServletRequest request,
                                   @Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {

        return userService.forgotPassword(request.getRequestURI(), forgotPasswordDto);
    }

    @GetMapping("/resetPassword/{resettoken}")
    public Optional verifyResetPassword(HttpServletRequest request,
                                        @PathVariable UUID resettoken) {

        return userService.verifyResetURL(resettoken.toString());
    }

    @PostMapping("/resetPassword/{resettoken}")
    public Optional resetPassword(@PathVariable UUID resettoken,
                                  @Valid @RequestBody SetNewPasswordDto setNewPasswordDto) {

        return userService.resetPassword(resettoken.toString(), setNewPasswordDto);
    }
}
