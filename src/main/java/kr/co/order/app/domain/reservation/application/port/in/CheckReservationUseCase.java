package kr.co.order.app.domain.reservation.application.port.in;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.util.ReservationStatus;

public interface CheckReservationUseCase {
    boolean existsReservationByRestaurantAndUserAndStatus(Restaurant restaurant, User user, ReservationStatus status);
}
