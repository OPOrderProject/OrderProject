package kr.co.order.app.domain.reservation.application.mapper;

import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.reservation.presentation.dto.CreateReservationDTO;
import kr.co.order.app.domain.reservation.presentation.dto.ResponseReservationDTO;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.util.ReservationStatus;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public Reservation toReservation(CreateReservationDTO dto, Restaurant restaurant, User user){
        return Reservation.builder()
                .restaurant(restaurant)
                .user(user)
                .reserverName(user.getNickname())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(ReservationStatus.SELECTED)
                .build();
    }

    public ResponseReservationDTO toResponseReservationDTO(Reservation reservation){
        return ResponseReservationDTO.builder()
                .id(reservation.getId())
                .restaurantName(reservation.getRestaurant().getName())
                .reserverName(reservation.getReserverName())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .status(reservation.getStatus())
                .build();
    }
}
