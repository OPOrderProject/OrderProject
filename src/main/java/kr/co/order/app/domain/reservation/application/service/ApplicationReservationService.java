package kr.co.order.app.domain.reservation.application.service;

import jakarta.persistence.OptimisticLockException;
import kr.co.order.app.domain.reservation.application.port.in.*;
import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.reservation.presentation.dto.CreateReservationDTO;
import kr.co.order.app.domain.reservation.presentation.dto.ModifyReservationDTO;
import kr.co.order.app.domain.reservation.presentation.dto.ResponseReservationDTO;
import kr.co.order.app.domain.restaurant.application.port.in.QueryRestaurantUseCase;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.timeSlot.application.port.in.QueryTimeSlotUseCase;
import kr.co.order.app.domain.timeSlot.application.port.in.UpdateTimeSlotUseCase;
import kr.co.order.app.domain.timeSlot.domain.TimeSlot;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.util.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationReservationService implements ReservationUseCase {
    private final CreateReservationUseCase createReservationUseCase;
    private final UpdateReservationUseCase updateReservationUseCase;
    private final QueryReservationUseCase queryReservationUseCase;
    private final CheckReservationUseCase checkReservationUseCase;
    private final QueryRestaurantUseCase queryRestaurantUseCase;
    private final UpdateTimeSlotUseCase updateTimeSlotUseCase;
    private final QueryTimeSlotUseCase queryTimeSlotUseCase;

    @Override
    public Reservation save(CreateReservationDTO dto, User user) {
        Restaurant restaurant = queryRestaurantUseCase.findById(dto.getRestaurantId());
        if(checkReservationUseCase.existsReservationByRestaurantAndUserAndStatus(restaurant, user, ReservationStatus.RESERVED)){
            throw new IllegalArgumentException("이미 금일 예약 이력이 있습니다.");
        }

        int maxRetry = 3;

        for(int attempt = 1; attempt<=maxRetry; attempt++){
            try {
                return reserveOnce(dto, restaurant, user);
            } catch (OptimisticLockException e){
                if(attempt == maxRetry){
                    throw new IllegalArgumentException("해당 시간대에 예약 요청이 많아 처리에 실패했습니다. 나중에 다시 시도해 주세요.");
                }
            }
        }
        throw new IllegalStateException("Unreachable");
    }

    @Override
    public void cancel(Long reservationId, User user) {
        int maxRetry = 3;

        for (int attempt = 1; attempt<=maxRetry; attempt++){
            try {
                cancelOnce(reservationId, user);
                return;
            } catch (OptimisticLockException e){
                if(attempt == maxRetry){
                    throw new IllegalArgumentException("현재 요청이 너무 많아 처리에 실패했습니다. 다시 시도해 주세요.");
                }
            }
        }
        throw new IllegalStateException("Unreachable");
    }

    @Override
    public Reservation modify(Long reservationId, ModifyReservationDTO dto, User user) {
        Reservation reservation = queryReservationUseCase.findById(reservationId);
        if(!reservation.certification(user)){
            throw new IllegalArgumentException("예약자 본인이 아닙니다.");
        }

        int maxRetry = 3;

        for (int attempt = 1; attempt<=maxRetry; attempt++){
            try {
                modifyOnce(reservation, dto);
                return reservation;
            } catch (OptimisticLockException e){
                if(attempt == maxRetry){
                    throw new IllegalArgumentException("현재 요청이 너무 많아 처리에 실패했습니다. 다시 시도해 주세요.");
                }
            }
        }
        throw new IllegalStateException("Unreachable");
    }

    @Override
    @Transactional
    public void confirmReservation(Long id, User user) {
        Reservation reservation = queryReservationUseCase.findById(id);

        updateReservationUseCase.updateStatus(reservation, ReservationStatus.RESERVED, user);
    }

    @Override
    public Page<ResponseReservationDTO> getReservationListByUser(int page, int size, String condition, User user) {
        Pageable pageable = switchCondition(page, size, condition);

        return queryReservationUseCase.findAllByUserForDTO(user, pageable);
    }

    @Override
    public Page<ResponseReservationDTO> getReservationListByRestaurant(int page, int size, String condition, Long restaurantId) {
        Pageable pageable = switchCondition(page, size, condition);
        Restaurant restaurant = queryRestaurantUseCase.findById(restaurantId);

        return queryReservationUseCase.findAllByRestaurantForDTO(restaurant, pageable);
    }

    @Override
    public ResponseReservationDTO getReservationById(Long id) {
        return queryReservationUseCase.findByIdForDTO(id);
    }

    @Transactional
    protected Reservation reserveOnce(CreateReservationDTO dto, Restaurant restaurant, User user){
        List<TimeSlot> slots = queryTimeSlotUseCase.findByRestaurantAndStartTime(restaurant, dto.getStartTime(), dto.getEndTime());
        updateTimeSlotUseCase.reserve(slots);

        return createReservationUseCase.save(dto, restaurant, user);
    }

    @Transactional
    protected void cancelOnce(Long reservationId, User user){
        Reservation reservation = queryReservationUseCase.findById(reservationId);
        if(!reservation.certification(user)){
            throw new IllegalArgumentException("예약자 본인이 아닙니다.");
        }

        List<TimeSlot> slots = queryTimeSlotUseCase.findByRestaurantAndStartTime(reservation.getRestaurant(),
                reservation.getStartTime(), reservation.getEndTime());
        updateTimeSlotUseCase.cancel(slots);
        updateReservationUseCase.updateStatus(reservation, ReservationStatus.CANCELED, user);
    }

    @Transactional
    protected void modifyOnce(Reservation reservation, ModifyReservationDTO dto){
        Restaurant restaurant = reservation.getRestaurant();

        List<TimeSlot> reserveSlots = queryTimeSlotUseCase.findByRestaurantAndStartTime(restaurant,
                dto.getStartTime(), dto.getEndTime());
        updateTimeSlotUseCase.reserve(reserveSlots);

        List<TimeSlot> cancelSlots = queryTimeSlotUseCase.findByRestaurantAndStartTime(restaurant,
                reservation.getStartTime(), reservation.getEndTime());
        updateTimeSlotUseCase.cancel(cancelSlots);

        updateReservationUseCase.updateReserveTime(reservation, dto.getStartTime(), dto.getEndTime());
    }

    public Pageable switchCondition(int page, int size, String condition){
        return switch (condition){
            case "older" ->
                    PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
            default ->
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        };
    }
}
