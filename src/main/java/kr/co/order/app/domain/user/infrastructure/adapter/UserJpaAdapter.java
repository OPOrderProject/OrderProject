package kr.co.order.app.domain.user.infrastructure.adapter;

import kr.co.order.app.domain.user.application.port.out.UserJpaPort;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.domain.user.infrastructure.repository.UserRepository;
import kr.co.order.app.global.exception.user.UserException;
import kr.co.order.app.global.exception.user.UserExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements UserJpaPort {
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User saveAndFlush(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
    }

    @Override
    public User findByUsernameOrNull(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    @Override
    public Optional<User> findByUsernameForOptional(String username) {
        return userRepository.findByUsername(username);
    }
}
