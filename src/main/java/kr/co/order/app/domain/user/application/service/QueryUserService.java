package kr.co.order.app.domain.user.application.service;

import kr.co.order.app.domain.user.application.port.in.QueryUserUseCase;
import kr.co.order.app.domain.user.application.port.out.UserJpaPort;
import kr.co.order.app.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueryUserService implements QueryUserUseCase {
    private final UserJpaPort userJpaPort;

    @Override
    public User findByUsername(String username) {
        return userJpaPort.findByUsername(username);
    }

    @Override
    public User findByUsernameOrNull(String username) {
        return userJpaPort.findByUsernameOrNull(username);
    }

    @Override
    public Optional<User> findByUsernameForOptional(String username) {
        return userJpaPort.findByUsernameForOptional(username);
    }
}
