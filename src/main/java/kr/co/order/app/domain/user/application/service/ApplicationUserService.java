package kr.co.order.app.domain.user.application.service;

import kr.co.order.app.domain.user.application.port.in.CreateUserUseCase;
import kr.co.order.app.domain.user.application.port.in.UserUseCase;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.domain.user.presentation.dto.CreateUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationUserService implements UserUseCase {
    private final CreateUserUseCase createUserUseCase;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public User save(CreateUserDTO dto) {
        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        return createUserUseCase.save(dto);
    }
}
