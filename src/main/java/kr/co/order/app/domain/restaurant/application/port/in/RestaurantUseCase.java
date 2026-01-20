package kr.co.order.app.domain.restaurant.application.port.in;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.restaurant.presentation.dto.CreateRestaurantDTO;
import kr.co.order.app.domain.restaurant.presentation.dto.ResponseRestaurantDTO;
import kr.co.order.app.domain.restaurant.presentation.dto.ResponseRestaurantInfoDTO;
import kr.co.order.app.domain.user.domain.User;
import org.springframework.data.domain.Page;

public interface RestaurantUseCase {
    Restaurant save(CreateRestaurantDTO dto, User user);
    Page<ResponseRestaurantDTO> getRestaurantList(int page, int size, String condition);
    ResponseRestaurantInfoDTO getRestaurantById(Long id);
}
