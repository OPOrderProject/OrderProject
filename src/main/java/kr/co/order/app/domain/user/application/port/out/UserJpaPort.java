package kr.co.order.app.domain.user.application.port.out;

import kr.co.order.app.domain.user.domain.User;

import java.util.Optional;

public interface UserJpaPort {
    User save(User user);
    User saveAndFlush(User user);
    User findByUsername(String username);
    User findByUsernameOrNull(String username);
    Optional<User> findByUsernameForOptional(String username);
}
