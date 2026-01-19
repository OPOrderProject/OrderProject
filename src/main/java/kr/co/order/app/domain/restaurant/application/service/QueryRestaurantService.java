package kr.co.order.app.domain.restaurant.application.service;

import kr.co.order.app.domain.restaurant.application.mapper.RestaurantMapper;
import kr.co.order.app.domain.restaurant.application.port.in.QueryRestaurantUseCase;
import kr.co.order.app.domain.restaurant.application.port.out.RestaurantJpaPort;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.restaurant.presentation.dto.ResponseRestaurantDTO;
import kr.co.order.app.domain.restaurant.presentation.dto.ResponseRestaurantInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryRestaurantService implements QueryRestaurantUseCase {
    private final RestaurantJpaPort restaurantJpaPort;
    private final RestaurantMapper restaurantMapper;

    @Override
    public Restaurant findById(Long id) {
        return restaurantJpaPort.findById(id);
    }

    @Override
    public ResponseRestaurantInfoDTO findByIdForDTO(Long id) {
        Restaurant restaurant = restaurantJpaPort.findById(id);
        List<LocalTime> slots = new ArrayList<>();
        LocalTime start = restaurant.getOpenTime();
        LocalTime end = restaurant.getCloseTime();
        int interval = 30;

        while (start.isBefore(end)){
            LocalTime next = start.plusMinutes(interval);
            slots.add(start);
            start = next;
        }

        return restaurantMapper.toResponseRestaurantInfoDTO(restaurant, slots);
    }

    @Override
    public Page<ResponseRestaurantDTO> findAllForDTO(Pageable pageable) {
        return restaurantJpaPort.findAll(pageable)
                .map(restaurant -> restaurantMapper.toResponseRestaurantDTO(restaurant));
    }
}
