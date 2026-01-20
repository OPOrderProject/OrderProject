package kr.co.order.app.domain.reservation.application.port.in;

import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.global.util.ReservationStatus;

import java.time.LocalTime;

public interface UpdateReservationUseCase {
    void updateStatus(Reservation reservation, ReservationStatus status);
    void updateReserveTime(Reservation reservation, LocalTime startTime, LocalTime endTime);
}
