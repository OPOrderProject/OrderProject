package kr.co.order.app.domain.user.application.service;

import kr.co.order.app.domain.user.application.port.in.CreateUserUseCase;
import kr.co.order.app.domain.user.application.port.out.UserJpaPort;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.domain.user.presentation.dto.CreateUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {
    private final UserJpaPort userJpaPort;
    private final UserDomainService userDomainService;

    @Override
    public User save(CreateUserDTO dto) {
        User user = userDomainService.createEntity(dto);

        return userJpaPort.save(user);
    }
}
