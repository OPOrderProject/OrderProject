package kr.co.order.app.domain.user.application.port.in;

import kr.co.order.app.domain.user.domain.User;

public interface UpdateUserUseCase {
    void updateRefreshToken(User user, String refreshToken);
}
