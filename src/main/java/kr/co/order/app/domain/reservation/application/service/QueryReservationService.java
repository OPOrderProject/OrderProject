package kr.co.order.app.domain.reservation.application.service;

import kr.co.order.app.domain.reservation.application.mapper.ReservationMapper;
import kr.co.order.app.domain.reservation.application.port.in.QueryReservationUseCase;
import kr.co.order.app.domain.reservation.application.port.out.ReservationJpaPort;
import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.reservation.presentation.dto.ResponseReservationDTO;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.util.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryReservationService implements QueryReservationUseCase {
    private final ReservationJpaPort reservationJpaPort;
    private final ReservationMapper reservationMapper;

    @Override
    public Reservation findById(Long id) {
        return reservationJpaPort.findById(id);
    }

    @Override
    public ResponseReservationDTO findByIdForDTO(Long id) {
        return reservationMapper.toResponseReservationDTO(reservationJpaPort.findById(id));
    }

    @Override
    public Page<ResponseReservationDTO> findAllByUserForDTO(User user, Pageable pageable) {
        return reservationJpaPort.findAllByUser(user, pageable)
                .map(reservation -> reservationMapper.toResponseReservationDTO(reservation));
    }

    @Override
    public Page<ResponseReservationDTO> findAllByRestaurantForDTO(Restaurant restaurant, Pageable pageable) {
        return reservationJpaPort.findAllByRestaurant(restaurant, pageable)
                .map(reservation -> reservationMapper.toResponseReservationDTO(reservation));
    }

    @Override
    public List<Reservation> findAllByStatusIsExpired(ReservationStatus status) {
        return reservationJpaPort.findAllByStatus(status).stream()
                .filter(reservation -> reservation.isExpired(LocalDateTime.now()))
                .toList();
    }
}
