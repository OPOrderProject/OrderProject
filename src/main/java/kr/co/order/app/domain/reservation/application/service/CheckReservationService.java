package kr.co.order.app.domain.reservation.application.service;

import kr.co.order.app.domain.reservation.application.port.in.CheckReservationUseCase;
import kr.co.order.app.domain.reservation.application.port.out.ReservationJpaPort;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.util.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckReservationService implements CheckReservationUseCase {
    private final ReservationJpaPort reservationJpaPort;

    @Override
    public boolean existsReservationByRestaurantAndUserAndStatus(Restaurant restaurant, User user, ReservationStatus status) {
        return reservationJpaPort.existsReservationByRestaurantAndUserAndStatus(restaurant, user, status);
    }
}
