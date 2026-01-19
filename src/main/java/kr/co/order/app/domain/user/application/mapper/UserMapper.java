package kr.co.order.app.domain.user.application.mapper;

import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.domain.user.presentation.dto.CreateUserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUser(CreateUserDTO dto){
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .build();
    }
}
