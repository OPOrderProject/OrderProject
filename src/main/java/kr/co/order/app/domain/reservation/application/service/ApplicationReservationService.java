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
import kr.co.order.app.global.exception.reservation.ReservationException;
import kr.co.order.app.global.exception.reservation.ReservationExceptionType;
import kr.co.order.app.global.exception.restaurant.RestaurantException;
import kr.co.order.app.global.exception.restaurant.RestaurantExceptionType;
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
            throw new ReservationException(ReservationExceptionType.RESERVATION_DUPLICATION_ERROR);
        }

        int maxRetry = 5;

        for(int attempt = 1; attempt<=maxRetry; attempt++){
            try {
                return reserveOnce(dto, restaurant, user);
            } catch (OptimisticLockException e){
                if(attempt == maxRetry){
                    throw new ReservationException(ReservationExceptionType.RESERVATION_TRAFFIC_ERROR);
                }
            }
        }
        throw new IllegalStateException("Unreachable");
    }

    @Override
    public void cancel(Long reservationId, User user) {
        int maxRetry = 5;

        for (int attempt = 1; attempt<=maxRetry; attempt++){
            try {
                cancelOnce(reservationId, user);
                return;
            } catch (OptimisticLockException e){
                if(attempt == maxRetry){
                    throw new ReservationException(ReservationExceptionType.RESERVATION_TRAFFIC_ERROR);
                }
            }
        }
        throw new IllegalStateException("Unreachable");
    }

    @Override
    public Reservation modify(Long reservationId, ModifyReservationDTO dto, User user) {
        Reservation reservation = queryReservationUseCase.findById(reservationId);
        if(!reservation.certification(user)){
            throw new ReservationException(ReservationExceptionType.NOT_AUTHORITY_UPDATE_RESERVATION);
        }

        int maxRetry = 5;

        for (int attempt = 1; attempt<=maxRetry; attempt++){
            try {
                modifyOnce(reservation, dto);
                return reservation;
            } catch (OptimisticLockException e){
                if(attempt == maxRetry){
                    throw new ReservationException(ReservationExceptionType.RESERVATION_TRAFFIC_ERROR);
                }
            }
        }
        throw new IllegalStateException("Unreachable");
    }

    @Override
    @Transactional
    public void confirmReservationByUser(Long id, User user) {
        Reservation reservation = queryReservationUseCase.findById(id);
        if(!reservation.certification(user)){
            throw new ReservationException(ReservationExceptionType.NOT_AUTHORITY_UPDATE_RESERVATION);
        }

        updateReservationUseCase.updateStatus(reservation, ReservationStatus.RESERVED);
    }

    @Override
    @Transactional
    public void confirmReservationByRestaurant(Long id, User user) {
        Reservation reservation = queryReservationUseCase.findById(id);
        Restaurant restaurant = reservation.getRestaurant();
        if(!restaurant.certification(user)){
            throw new RestaurantException(RestaurantExceptionType.RESTAURANT_AUTHORITY_ERROR);
        }

        updateReservationUseCase.updateStatus(reservation, ReservationStatus.COMPLETE);
    }

    @Override
    public Page<ResponseReservationDTO> getReservationListByUser(int page, int size, String condition, User user) {
        Pageable pageable = switchCondition(page, size, condition);

        return queryReservationUseCase.findAllByUserForDTO(user, pageable);
    }

    @Override
    public Page<ResponseReservationDTO> getReservationListByRestaurant(int page, int size, String condition, Long restaurantId, User user) {
        Restaurant restaurant = queryRestaurantUseCase.findById(restaurantId);
        if(!restaurant.certification(user)){
            throw new RestaurantException(RestaurantExceptionType.RESTAURANT_AUTHORITY_ERROR);
        }
        Pageable pageable = switchCondition(page, size, condition);

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
            throw new ReservationException(ReservationExceptionType.NOT_AUTHORITY_UPDATE_RESERVATION);
        }

        List<TimeSlot> slots = queryTimeSlotUseCase.findByRestaurantAndStartTime(reservation.getRestaurant(),
                reservation.getStartTime(), reservation.getEndTime());
        updateTimeSlotUseCase.cancel(slots);
        updateReservationUseCase.updateStatus(reservation, ReservationStatus.CANCELED);
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
