package kr.co.order.app.domain.timeSlot.application.port.in;

import kr.co.order.app.domain.restaurant.domain.Restaurant;

public interface CreateTimeSlotUseCase {
    void save(Restaurant restaurant);
}
