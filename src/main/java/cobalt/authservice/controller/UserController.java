package cobalt.authservice.controller;

import cobalt.authservice.dto.UserMapper;
import cobalt.authservice.entity.User;
import cobalt.authservice.dto.UserDto;
import cobalt.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        User user = UserMapper.DtoToUser(userDto);
        return UserMapper.userToDto(userService.createUser(user));
    }

}
