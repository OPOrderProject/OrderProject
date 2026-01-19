package kr.co.order.app.domain.restaurant.application.service;

import kr.co.order.app.domain.restaurant.application.mapper.RestaurantMapper;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.restaurant.presentation.dto.CreateRestaurantDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantDomainService {
    private final RestaurantMapper restaurantMapper;

    public Restaurant createEntity(CreateRestaurantDTO dto){
        return restaurantMapper.toRestaurant(dto);
    }
}
