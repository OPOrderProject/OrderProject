package kr.co.order.app.domain.restaurant.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRestaurantInfoDTO {
    private Long id;
    private String name;
    private int maxReservationCount;
    private List<LocalTime> slots;
}
