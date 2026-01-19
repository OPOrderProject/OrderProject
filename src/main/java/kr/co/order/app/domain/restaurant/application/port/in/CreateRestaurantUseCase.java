package kr.co.order.app.domain.restaurant.application.port.in;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.restaurant.presentation.dto.CreateRestaurantDTO;

public interface CreateRestaurantUseCase {
    Restaurant save(CreateRestaurantDTO dto);
}
