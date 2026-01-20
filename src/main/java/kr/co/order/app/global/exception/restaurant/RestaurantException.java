package kr.co.order.app.global.exception.restaurant;

import kr.co.order.app.global.exception.BaseException;
import kr.co.order.app.global.exception.ExceptionType;

public class RestaurantException extends RuntimeException implements BaseException {
    private ExceptionType exceptionType;

    public RestaurantException(ExceptionType exceptionType){
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public ExceptionType getExceptionType() {
        return this.exceptionType;
    }
}
