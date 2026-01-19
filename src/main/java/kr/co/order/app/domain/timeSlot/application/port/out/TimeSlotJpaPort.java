package kr.co.order.app.domain.timeSlot.application.port.out;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.timeSlot.domain.TimeSlot;

import java.time.LocalTime;
import java.util.List;

public interface TimeSlotJpaPort {
    TimeSlot save(TimeSlot timeSlot);
    void saveAll(List<TimeSlot> slots);
    void saveAndFlush(TimeSlot timeSlot);
    TimeSlot findByRestaurantAndStartTime(Restaurant restaurant, LocalTime startTime);
}
