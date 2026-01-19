package kr.co.order.app.domain.reservation.application.port.in;

import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.reservation.presentation.dto.ResponseReservationDTO;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.util.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QueryReservationUseCase {
    Reservation findById(Long id);
    ResponseReservationDTO findByIdForDTO(Long id);
    Page<ResponseReservationDTO> findAllByUserForDTO(User user, Pageable pageable);
    Page<ResponseReservationDTO> findAllByRestaurantForDTO(Restaurant restaurant, Pageable pageable);
    List<Reservation> findAllByStatusIsExpired(ReservationStatus status);
}
