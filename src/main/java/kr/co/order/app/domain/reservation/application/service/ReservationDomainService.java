package kr.co.order.app.domain.reservation.application.service;

import kr.co.order.app.domain.reservation.application.mapper.ReservationMapper;
import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.reservation.presentation.dto.CreateReservationDTO;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.util.AuthorCertification;
import kr.co.order.app.global.util.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ReservationDomainService {
    private final ReservationMapper reservationMapper;

    public Reservation createEntity(CreateReservationDTO dto, Restaurant restaurant, User user){
        return reservationMapper.toReservation(dto, restaurant, user);
    }

    public void updateStatus(Reservation reservation, ReservationStatus status){
        reservation.updateStatus(status);
    }

    public void updateReserveTime(Reservation reservation, LocalTime startTime, LocalTime endTime){
        reservation.updateReserveTime(startTime, endTime);
    }

    public boolean certification(Reservation reservation, User user){
        return AuthorCertification.certification(reservation.getReserverName(), user.getNickname());
    }
}
