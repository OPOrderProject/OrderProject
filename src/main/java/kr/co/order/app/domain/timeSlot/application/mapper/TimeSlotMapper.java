package kr.co.order.app.domain.timeSlot.application.mapper;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.timeSlot.domain.TimeSlot;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class TimeSlotMapper {
    public TimeSlot toTimeSlot(Restaurant restaurant, LocalTime startTime, LocalTime endTime){
        return TimeSlot.builder()
                .restaurant(restaurant)
                .startTime(startTime)
                .endTime(endTime)
                .reservedCount(restaurant.getMaxReservationCount())
                .build();
    }
}
