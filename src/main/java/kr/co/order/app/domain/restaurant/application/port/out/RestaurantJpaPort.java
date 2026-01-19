package kr.co.order.app.domain.restaurant.application.port.out;

import kr.co.order.app.domain.restaurant.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantJpaPort {
    Restaurant save(Restaurant restaurant);
    Restaurant findById(Long id);
    Page<Restaurant> findAll(Pageable pageable);
}
