package kr.co.order.app.domain.timeSlot.application.service;

import kr.co.order.app.domain.timeSlot.application.port.in.UpdateTimeSlotUseCase;
import kr.co.order.app.domain.timeSlot.domain.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateTimeSlotService implements UpdateTimeSlotUseCase {
    private final TimeSlotDomainService timeSlotDomainService;

    @Override
    public void reserve(List<TimeSlot> slots) {
        slots.sort(Comparator.comparing(TimeSlot::getStartTime));
        for (TimeSlot timeSlot : slots){
            timeSlotDomainService.reserve(timeSlot);
        }
    }

    @Override
    public void cancel(List<TimeSlot> slots) {
        slots.sort(Comparator.comparing(TimeSlot::getStartTime));
        for (TimeSlot timeSlot : slots){
            timeSlotDomainService.cancel(timeSlot);
        }
    }
}
