package kr.co.order.app.domain.reservation.infrastructure.adapter;

import kr.co.order.app.domain.reservation.application.port.out.ReservationJpaPort;
import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.reservation.infrastructure.repository.ReservationRepository;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.util.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationJpaAdapter implements ReservationJpaPort {
    private final ReservationRepository reservationRepository;

    @Override
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public void saveAndFlush(Reservation reservation) {
        reservationRepository.saveAndFlush(reservation);
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약건이 존재하지 않습니다."));
    }

    @Override
    public Page<Reservation> findAllByUser(User user, Pageable pageable) {
        return reservationRepository.findAllByUser(user, pageable);
    }

    @Override
    public Page<Reservation> findAllByRestaurant(Restaurant restaurant, Pageable pageable) {
        return reservationRepository.findAllByRestaurant(restaurant, pageable);
    }

    @Override
    public List<Reservation> findAllByStatus(ReservationStatus status) {
        return reservationRepository.findAllByStatus(status);
    }

    @Override
    public boolean existsReservationByRestaurantAndUserAndStatus(Restaurant restaurant, User user, ReservationStatus status) {
        return reservationRepository.existsReservationByRestaurantAndUserAndStatus(restaurant, user, status);
    }
}
