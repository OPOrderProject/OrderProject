package kr.co.order.app.domain.timeSlot.application.port.in;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.timeSlot.domain.TimeSlot;

import java.time.LocalTime;
import java.util.List;

public interface QueryTimeSlotUseCase {
    List<TimeSlot> findByRestaurantAndStartTime(Restaurant restaurant, LocalTime startTime, LocalTime endTime);
}
