package kr.co.order.app.domain.reservation.infrastructure.repository;

import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.util.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findAllByUser(User user, Pageable pageable);
    Page<Reservation> findAllByRestaurant(Restaurant restaurant, Pageable pageable);
    List<Reservation> findAllByStatus(ReservationStatus status);
    boolean existsReservationByRestaurantAndUserAndStatus(Restaurant restaurant, User user, ReservationStatus status);
}
