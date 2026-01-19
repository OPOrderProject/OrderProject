package kr.co.order.app.domain.timeSlot.application.service;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.timeSlot.application.port.in.QueryTimeSlotUseCase;
import kr.co.order.app.domain.timeSlot.application.port.out.TimeSlotJpaPort;
import kr.co.order.app.domain.timeSlot.domain.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryTimeSlotService implements QueryTimeSlotUseCase {
    private final TimeSlotJpaPort timeSlotJpaPort;

    @Override
    public List<TimeSlot> findByRestaurantAndStartTime(Restaurant restaurant, LocalTime startTime, LocalTime endTime) {
        List<TimeSlot> slots = new ArrayList<>();
        int interval = 30;

        while (startTime.isBefore(endTime)){
            LocalTime next = startTime.plusMinutes(interval);

            slots.add(timeSlotJpaPort.findByRestaurantAndStartTime(restaurant, startTime));

            startTime = next;
        }

        return slots;
    }
}
