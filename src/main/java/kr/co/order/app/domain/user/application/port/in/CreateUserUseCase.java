package kr.co.order.app.domain.user.application.port.in;

import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.domain.user.presentation.dto.CreateUserDTO;

public interface CreateUserUseCase {
    User save(CreateUserDTO dto);
}
