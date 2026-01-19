package kr.co.order.app.domain.restaurant.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRestaurantDTO {
    private Long id;
    private String name;
    private int maxReservationCount;
}
