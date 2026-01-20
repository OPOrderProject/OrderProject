package kr.co.order.app.domain.restaurant.application.service;

import kr.co.order.app.domain.restaurant.application.port.in.CreateRestaurantUseCase;
import kr.co.order.app.domain.restaurant.application.port.in.QueryRestaurantUseCase;
import kr.co.order.app.domain.restaurant.application.port.in.RestaurantUseCase;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.restaurant.presentation.dto.CreateRestaurantDTO;
import kr.co.order.app.domain.restaurant.presentation.dto.ResponseRestaurantDTO;
import kr.co.order.app.domain.restaurant.presentation.dto.ResponseRestaurantInfoDTO;
import kr.co.order.app.domain.timeSlot.application.port.in.CreateTimeSlotUseCase;
import kr.co.order.app.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationRestaurantService implements RestaurantUseCase {
    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final QueryRestaurantUseCase queryRestaurantUseCase;
    private final CreateTimeSlotUseCase createTimeSlotUseCase;

    @Override
    @Transactional
    public Restaurant save(CreateRestaurantDTO dto, User user) {
        Restaurant restaurant = createRestaurantUseCase.save(dto, user);
        createTimeSlotUseCase.save(restaurant);

        return restaurant;
    }

    @Override
    public Page<ResponseRestaurantDTO> getRestaurantList(int page, int size, String condition) {
        Pageable pageable = switchCondition(page, size, condition);

        return queryRestaurantUseCase.findAllForDTO(pageable);
    }

    @Override
    public ResponseRestaurantInfoDTO getRestaurantById(Long id) {
        return queryRestaurantUseCase.findByIdForDTO(id);
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
