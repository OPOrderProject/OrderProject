package kr.co.order.app.domain.reservation.application.port.in;

import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.reservation.presentation.dto.CreateReservationDTO;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.user.domain.User;

public interface CreateReservationUseCase {
    Reservation save(CreateReservationDTO dto, Restaurant restaurant, User user);
}
