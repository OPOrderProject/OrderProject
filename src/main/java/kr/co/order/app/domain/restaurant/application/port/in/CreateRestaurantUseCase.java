package kr.co.order.app.domain.restaurant.application.port.in;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.restaurant.presentation.dto.CreateRestaurantDTO;
import kr.co.order.app.domain.user.domain.User;

public interface CreateRestaurantUseCase {
    Restaurant save(CreateRestaurantDTO dto, User user);
}
