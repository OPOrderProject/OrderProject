package kr.co.order.app.global.exception.user;

import kr.co.order.app.global.exception.BaseException;
import kr.co.order.app.global.exception.ExceptionType;

public class UserException extends RuntimeException implements BaseException {
    private ExceptionType exceptionType;

    public UserException(ExceptionType exceptionType){
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public ExceptionType getExceptionType() {
        return this.exceptionType;
    }
}
