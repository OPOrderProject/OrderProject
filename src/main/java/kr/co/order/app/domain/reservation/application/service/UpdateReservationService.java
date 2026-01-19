package kr.co.order.app.domain.reservation.application.service;

import kr.co.order.app.domain.reservation.application.port.in.UpdateReservationUseCase;
import kr.co.order.app.domain.reservation.application.port.out.ReservationJpaPort;
import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.util.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class UpdateReservationService implements UpdateReservationUseCase {
    private final ReservationJpaPort reservationJpaPort;
    private final ReservationDomainService reservationDomainService;

    // save 제거 고민
    @Override
    public void updateStatus(Reservation reservation, ReservationStatus status, User user) {
        if(!reservationDomainService.certification(reservation, user)){
            throw new IllegalArgumentException("예약자 본인이 아닙니다.");
        }
        reservationDomainService.updateStatus(reservation, status);

        reservationJpaPort.saveAndFlush(reservation);
    }

    @Override
    public void updateReserveTime(Reservation reservation, LocalTime startTime, LocalTime endTime) {
        reservationDomainService.updateReserveTime(reservation, startTime, endTime);

        reservationJpaPort.saveAndFlush(reservation);
    }

    @Override
    public void schedulerCancelReservation(Reservation reservation, ReservationStatus status) {
        reservationDomainService.updateStatus(reservation, status);

        reservationJpaPort.saveAndFlush(reservation);
    }
}
