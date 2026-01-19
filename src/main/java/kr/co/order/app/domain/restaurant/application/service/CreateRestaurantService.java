package kr.co.order.app.domain.restaurant.application.service;

import kr.co.order.app.domain.restaurant.application.port.in.CreateRestaurantUseCase;
import kr.co.order.app.domain.restaurant.application.port.out.RestaurantJpaPort;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.restaurant.presentation.dto.CreateRestaurantDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRestaurantService implements CreateRestaurantUseCase {
    private final RestaurantJpaPort restaurantJpaPort;
    private final RestaurantDomainService restaurantDomainService;

    @Override
    public Restaurant save(CreateRestaurantDTO dto) {
        Restaurant restaurant = restaurantDomainService.createEntity(dto);

        return restaurantJpaPort.save(restaurant);
    }
}
