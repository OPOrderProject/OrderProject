package kr.co.order.app.domain.reservation.application.service;

import kr.co.order.app.domain.reservation.application.port.in.CreateReservationUseCase;
import kr.co.order.app.domain.reservation.application.port.out.ReservationJpaPort;
import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.reservation.presentation.dto.CreateReservationDTO;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateReservationService implements CreateReservationUseCase {
    private final ReservationJpaPort reservationJpaPort;
    private final ReservationDomainService reservationDomainService;

    @Override
    public Reservation save(CreateReservationDTO dto, Restaurant restaurant, User user) {
        Reservation reservation = reservationDomainService.createEntity(dto, restaurant, user);

        return reservationJpaPort.save(reservation);
    }
}
