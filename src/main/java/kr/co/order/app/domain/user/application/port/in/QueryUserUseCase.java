package kr.co.order.app.domain.user.application.port.in;

import kr.co.order.app.domain.user.domain.User;

import java.util.Optional;

public interface QueryUserUseCase {
    User findByUsername(String username);
    User findByUsernameOrNull(String username);
    Optional<User> findByUsernameForOptional(String username);
}
