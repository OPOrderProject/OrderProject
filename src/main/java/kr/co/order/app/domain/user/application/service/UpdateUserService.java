package kr.co.order.app.domain.user.application.service;

import kr.co.order.app.domain.user.application.port.in.UpdateUserUseCase;
import kr.co.order.app.domain.user.application.port.out.UserJpaPort;
import kr.co.order.app.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserService implements UpdateUserUseCase {
    private final UserJpaPort userJpaPort;
    private final UserDomainService userDomainService;

    @Override
    public void updateRefreshToken(User user, String refreshToken) {
        userDomainService.updateRefreshToken(user, refreshToken);
        userJpaPort.saveAndFlush(user);
    }
}
