package kr.co.order.app.domain.reservation.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.order.app.domain.reservation.application.port.in.ReservationUseCase;
import kr.co.order.app.domain.reservation.domain.Reservation;
import kr.co.order.app.domain.reservation.presentation.dto.CreateReservationDTO;
import kr.co.order.app.domain.reservation.presentation.dto.ModifyReservationDTO;
import kr.co.order.app.domain.reservation.presentation.dto.ResponseReservationDTO;
import kr.co.order.app.global.config.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
@Tag(name = "Reservation Controller", description = "예약 관련 서비스")
public class ReservationController {
    private final ReservationUseCase reservationUseCase;

    @PostMapping("/save")
    @Operation(summary = "식당 예약", description = "특정 식당 및 특정 시간에 예약 추가(저장)")
    public ResponseEntity<Reservation> save(@RequestBody CreateReservationDTO dto, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationUseCase.save(dto, principalDetails.getUser()));
    }

    @GetMapping("/list/user")
    @Operation(summary = "예약 목록(사용자)", description = "로그인 유저의 예약 목록 반환")
    public ResponseEntity<List<ResponseReservationDTO>> getReservationListByUser(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                 @RequestParam(value = "condition", defaultValue = "new") String condition){
        Page<ResponseReservationDTO> newPage = reservationUseCase.getReservationListByUser(page, size, condition, principalDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(newPage.getContent());
    }

    @GetMapping("/list/restaurant/{id}")
    @Operation(summary = "예약 목록(레스토랑)", description = "특정 레스토랑의 예약 목록 반환")
    public ResponseEntity<List<ResponseReservationDTO>> getReservationListByRestaurant(@PathVariable Long id,
                                                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                       @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                       @RequestParam(value = "condition", defaultValue = "new") String condition){
        Page<ResponseReservationDTO> newPage = reservationUseCase.getReservationListByRestaurant(page, size, condition, id);
        return ResponseEntity.status(HttpStatus.OK).body(newPage.getContent());
    }

    @GetMapping("/find/{id}")
    @Operation(summary = "예약 상세 정보", description = "특정 예약의 상세 정보 반환")
    public ResponseEntity<ResponseReservationDTO> getReservationById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(reservationUseCase.getReservationById(id));
    }

    @PatchMapping("/reservation-confirm/{id}")
    @Operation(summary = "예약 확인", description = "예약 시간 선택 후 최종 예약 확인")
    public ResponseEntity<Void> confirmReservation(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        reservationUseCase.confirmReservation(id, principalDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/modify/{id}")
    @Operation(summary = "예약 변경", description = "특정 예약의 시간대를 변경 및 저장")
    public ResponseEntity<Reservation> modify(@PathVariable Long id, @RequestBody ModifyReservationDTO dto,
                                              @AuthenticationPrincipal PrincipalDetails principalDetails){
        return ResponseEntity.status(HttpStatus.OK).body(reservationUseCase.modify(id, dto, principalDetails.getUser()));
    }

    @PatchMapping("/cancel/{id}")
    @Operation(summary = "예약 취소", description = "특정 예약을 취소하고 예약 도메인 상태 변경")
    public ResponseEntity<Void> cancel(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        reservationUseCase.cancel(id, principalDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
