package kr.co.order.app.domain.timeSlot.infrastructure.adapter;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.timeSlot.application.port.out.TimeSlotJpaPort;
import kr.co.order.app.domain.timeSlot.domain.TimeSlot;
import kr.co.order.app.domain.timeSlot.infrastructure.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TimeSlotJpaAdapter implements TimeSlotJpaPort {
    private final TimeSlotRepository timeSlotRepository;

    @Override
    public TimeSlot save(TimeSlot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }

    @Override
    public void saveAll(List<TimeSlot> slots) {
        timeSlotRepository.saveAll(slots);
    }

    @Override
    public void saveAndFlush(TimeSlot timeSlot) {
        timeSlotRepository.saveAndFlush(timeSlot);
    }

    @Override
    public TimeSlot findByRestaurantAndStartTime(Restaurant restaurant, LocalTime startTime) {
        return timeSlotRepository.findByRestaurantAndStartTime(restaurant, startTime);
    }
}
