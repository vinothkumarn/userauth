package cobalt.authservice.dto;

import cobalt.authservice.entity.User;

public class UserMapper {

    public static UserDto userToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .dateCreated(user.getDateCreated())
                .build();
    }

    public static User DtoToUser(UserDto userDto) {
        return User.builder()
                .userName(userDto.getUserName())
                .password(userDto.getPassword())
                .build();
    }

}
