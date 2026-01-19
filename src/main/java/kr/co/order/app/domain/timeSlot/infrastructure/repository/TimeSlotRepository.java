package kr.co.order.app.domain.timeSlot.infrastructure.repository;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.timeSlot.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    TimeSlot findByRestaurantAndStartTime(Restaurant restaurant, LocalTime startTime);
}
