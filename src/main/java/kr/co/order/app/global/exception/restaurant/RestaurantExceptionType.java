package kr.co.order.app.global.exception.restaurant;

import kr.co.order.app.global.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum RestaurantExceptionType implements ExceptionType {
    RESTAURANT_NOT_FOUND("RESTAURANT_NOT_FOUND", HttpStatus.NOT_FOUND, "레스토랑을 찾을 수 없습니다."),
    RESTAURANT_AUTHORITY_ERROR("RESTAURANT_AUTHORITY_ERROR", HttpStatus.FORBIDDEN, "권한이 없습니다.");

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
