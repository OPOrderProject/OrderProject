package kr.co.order.app.global.exception.reservation;

import kr.co.order.app.global.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ReservationExceptionType implements ExceptionType {
    RESERVATION_NOT_FOUND("RESERVATION_NOT_FOUND", HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."),
    RESERVATION_DUPLICATION_ERROR("RESERVATION_DUPLICATION_ERROR", HttpStatus.BAD_REQUEST, "이미 금일 예약 이력이 있습니다."),
    RESERVATION_TRAFFIC_ERROR("RESERVATION_TRAFFIC_ERROR", HttpStatus.TOO_MANY_REQUESTS, "현재 요청이 너무 많아 처리에 실패했습니다. 다시 시도해 주세요."),
    NOT_AUTHORITY_UPDATE_RESERVATION("NOT_AUTHORITY_UPDATE_RESERVATION", HttpStatus.FORBIDDEN, "예약 업데이트 권한이 없습니다.");

    private final String errorName;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrorName() {
        return this.errorName;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
