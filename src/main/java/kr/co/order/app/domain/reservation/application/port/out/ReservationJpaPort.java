package kr.co.order.app.domain.reservation.application.port.out;

import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.util.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationJpaPort {
    Reservation save(Reservation reservation);
    void saveAndFlush(Reservation reservation);
    Reservation findById(Long id);
    Page<Reservation> findAllByUser(User user, Pageable pageable);
    Page<Reservation> findAllByRestaurant(Restaurant restaurant, Pageable pageable);
    List<Reservation> findAllByStatus(ReservationStatus status);
    boolean existsReservationByRestaurantAndUserAndStatus(Restaurant restaurant, User user, ReservationStatus status);
}
