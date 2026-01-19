package kr.co.order.app.domain.timeSlot.application.service;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.timeSlot.application.mapper.TimeSlotMapper;
import kr.co.order.app.domain.timeSlot.domain.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TimeSlotDomainService {
    private final TimeSlotMapper timeSlotMapper;

    public TimeSlot createEntity(Restaurant restaurant, LocalTime startTime, LocalTime endTime){
        return timeSlotMapper.toTimeSlot(restaurant, startTime, endTime);
    }

    public void reserve(TimeSlot timeSlot){
        timeSlot.reserve();
    }

    public void cancel(TimeSlot timeSlot){
        timeSlot.cancel();
    }
}
