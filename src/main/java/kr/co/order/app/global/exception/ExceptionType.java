package kr.co.order.app.global.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionType {
    String getErrorName();
    HttpStatus getHttpStatus();
    String getMessage();
}
