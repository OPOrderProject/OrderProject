package kr.co.order.app.domain.restaurant.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.order.app.domain.restaurant.application.port.in.RestaurantUseCase;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.restaurant.presentation.dto.CreateRestaurantDTO;
import kr.co.order.app.domain.restaurant.presentation.dto.ResponseRestaurantDTO;
import kr.co.order.app.domain.restaurant.presentation.dto.ResponseRestaurantInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurant")
@Tag(name = "Restaurant Controller", description = "레스토랑(식당) 관련 서비스")
public class RestaurantController {
    private final RestaurantUseCase restaurantUseCase;

    @PostMapping("/save")
    @Operation(summary = "레스토랑 등록", description = "레스토랑 정보를 받아 저장")
    public ResponseEntity<Restaurant> save(@Valid @RequestBody CreateRestaurantDTO createRestaurantDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantUseCase.save(createRestaurantDTO));
    }

    @GetMapping("/find-all")
    @Operation(summary = "레스토랑 리스트", description = "전체 레스토랑 리스트 반환")
    public ResponseEntity<List<ResponseRestaurantDTO>> getRestaurantList(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                                                         @RequestParam(value = "condition", defaultValue = "new") String condition){
        Page<ResponseRestaurantDTO> newPage = restaurantUseCase.getRestaurantList(page, size, condition);
        return ResponseEntity.status(HttpStatus.OK).body(newPage.getContent());
    }

    @GetMapping("/find/{id}")
    @Operation(summary = "레스토랑 정보", description = "특정 레스토랑의 상세 정보 반환")
    public ResponseEntity<ResponseRestaurantInfoDTO> getRestaurantById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(restaurantUseCase.getRestaurantById(id));
    }
}
