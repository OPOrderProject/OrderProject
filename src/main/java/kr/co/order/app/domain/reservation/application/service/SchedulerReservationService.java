package kr.co.order.app.domain.reservation.application.service;

import jakarta.persistence.OptimisticLockException;
import kr.co.order.app.domain.reservation.application.port.in.QueryReservationUseCase;
import kr.co.order.app.domain.reservation.application.port.in.UpdateReservationUseCase;
import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.timeSlot.application.port.in.QueryTimeSlotUseCase;
import kr.co.order.app.domain.timeSlot.application.port.in.UpdateTimeSlotUseCase;
import kr.co.order.app.domain.timeSlot.domain.TimeSlot;
import kr.co.order.app.global.util.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerReservationService {
    private final UpdateReservationUseCase updateReservationUseCase;
    private final QueryReservationUseCase queryReservationUseCase;
    private final UpdateTimeSlotUseCase updateTimeSlotUseCase;
    private final QueryTimeSlotUseCase queryTimeSlotUseCase;

    @Scheduled(fixedRate = 60000)
    public void expireReservation(){
        List<Reservation> reservationList = queryReservationUseCase.findAllByStatusIsExpired(ReservationStatus.SELECTED);

        for (Reservation reservation : reservationList){
            try {
                cancelByScheduler(reservation.getId());
            } catch (OptimisticLockException e){

            } catch (Exception e){

            }
        }
    }

    public void cancelByScheduler(Long reservationId) {

        int maxRetry = 5;

        for (int attempt = 1; attempt <= maxRetry; attempt++) {
            try {
                cancelOnce(reservationId);
                return;
            } catch (OptimisticLockException e) {
                if (attempt == maxRetry) {
                    throw e;
                }
            }
        }
    }

    @Transactional
    protected void cancelOnce(Long reservationId){
        Reservation reservation = queryReservationUseCase.findById(reservationId);

        List<TimeSlot> slots = queryTimeSlotUseCase.findByRestaurantAndStartTime(reservation.getRestaurant(),
                reservation.getStartTime(), reservation.getEndTime());
        updateTimeSlotUseCase.cancel(slots);
        updateReservationUseCase.updateStatus(reservation, ReservationStatus.CANCELED);
    }
}
