package kr.co.order.app.global.exception.reservation;

import kr.co.order.app.global.exception.BaseException;
import kr.co.order.app.global.exception.ExceptionType;

public class ReservationException extends RuntimeException implements BaseException {
    private ExceptionType exceptionType;

    public ReservationException(ExceptionType exceptionType){
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public ExceptionType getExceptionType() {
        return this.exceptionType;
    }
}
