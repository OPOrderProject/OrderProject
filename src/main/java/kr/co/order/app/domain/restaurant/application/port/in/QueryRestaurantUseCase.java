package kr.co.order.app.domain.restaurant.application.port.in;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.restaurant.presentation.dto.ResponseRestaurantDTO;
import kr.co.order.app.domain.restaurant.presentation.dto.ResponseRestaurantInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryRestaurantUseCase {
    Restaurant findById(Long id);
    ResponseRestaurantInfoDTO findByIdForDTO(Long id);
    Page<ResponseRestaurantDTO> findAllForDTO(Pageable pageable);
}
