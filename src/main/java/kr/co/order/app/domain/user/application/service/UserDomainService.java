package kr.co.order.app.domain.user.application.service;

import kr.co.order.app.domain.user.application.mapper.UserMapper;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.domain.user.presentation.dto.CreateUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDomainService {
    private final UserMapper userMapper;

    public User createEntity(CreateUserDTO dto){
        return userMapper.toUser(dto);
    }

    public void updateRefreshToken(User user, String refreshToken){
        user.updateRefreshToken(refreshToken);
    }
}
