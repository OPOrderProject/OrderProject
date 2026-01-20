package kr.co.order.app.domain.reservation.application.port.in;

import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.reservation.presentation.dto.CreateReservationDTO;
import kr.co.order.app.domain.reservation.presentation.dto.ModifyReservationDTO;
import kr.co.order.app.domain.reservation.presentation.dto.ResponseReservationDTO;
import kr.co.order.app.domain.user.domain.User;
import org.springframework.data.domain.Page;

public interface ReservationUseCase {
    Reservation save(CreateReservationDTO dto, User user);
    void cancel(Long reservationId, User user);
    Reservation modify(Long reservationId, ModifyReservationDTO dto, User user);
    void confirmReservationByUser(Long id, User user);
    void confirmReservationByRestaurant(Long id, User user);
    Page<ResponseReservationDTO> getReservationListByUser(int page, int size, String condition, User user);
    Page<ResponseReservationDTO> getReservationListByRestaurant(int page, int size, String condition, Long restaurantId, User user);
    ResponseReservationDTO getReservationById(Long id);
}
