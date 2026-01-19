package kr.co.order.app.domain.timeSlot.application.service;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.timeSlot.application.port.in.CreateTimeSlotUseCase;
import kr.co.order.app.domain.timeSlot.application.port.out.TimeSlotJpaPort;
import kr.co.order.app.domain.timeSlot.domain.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateTimeSlotService implements CreateTimeSlotUseCase {
    private final TimeSlotJpaPort timeSlotJpaPort;
    private final TimeSlotDomainService timeSlotDomainService;

    @Override
    public void save(Restaurant restaurant) {
        LocalTime start = restaurant.getOpenTime();
        LocalTime end = restaurant.getCloseTime();
        int interval = 30;

        List<TimeSlot> slots = new ArrayList<>();

        while (start.isBefore(end)){
            LocalTime slotEnd = start.plusMinutes(interval);

            slots.add(timeSlotDomainService.createEntity(restaurant, start, slotEnd));

            start = slotEnd;
        }

        timeSlotJpaPort.saveAll(slots);
    }
}
