package com.jo2k.garagify.config.exceptionHandling;

import com.jo2k.garagify.common.exception.InvalidBorrow;
import com.jo2k.garagify.common.exception.InvalidTokenException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Hidden
class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidBorrow.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(InvalidBorrow exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
