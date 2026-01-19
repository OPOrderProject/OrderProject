package kr.co.order.app.domain.timeSlot.application.port.in;

import kr.co.order.app.domain.timeSlot.domain.TimeSlot;

import java.util.List;

public interface UpdateTimeSlotUseCase {
    void reserve(List<TimeSlot> slots);
    void cancel(List<TimeSlot> slots);
}
