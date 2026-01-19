package kr.co.order.app.domain.restaurant.application.mapper;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.restaurant.presentation.dto.CreateRestaurantDTO;
import kr.co.order.app.domain.restaurant.presentation.dto.ResponseRestaurantDTO;
import kr.co.order.app.domain.restaurant.presentation.dto.ResponseRestaurantInfoDTO;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class RestaurantMapper {
    public Restaurant toRestaurant(CreateRestaurantDTO dto){
        return Restaurant.builder()
                .name(dto.getName())
                .maxReservationCount(dto.getMaxReservationCount())
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .build();
    }

    public ResponseRestaurantDTO toResponseRestaurantDTO(Restaurant restaurant){
        return ResponseRestaurantDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .maxReservationCount(restaurant.getMaxReservationCount())
                .build();
    }

    public ResponseRestaurantInfoDTO toResponseRestaurantInfoDTO(Restaurant restaurant, List<LocalTime> slots){
        return ResponseRestaurantInfoDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .maxReservationCount(restaurant.getMaxReservationCount())
                .slots(slots)
                .build();
    }
}
