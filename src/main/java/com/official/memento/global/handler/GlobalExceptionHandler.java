package com.official.memento.global.handler;

import com.official.memento.global.dto.ErrorResponse;
import com.official.memento.global.exception.EntityNotFoundException;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidRequestBodyException;
import com.official.memento.global.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception exception) {
        logger.error("Unhandled exception occurred2 ", exception);
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<ErrorResponse> invalidRequestBodyException(InvalidRequestBodyException exception) {
        logger.warn("Invalid request body: {}", exception.getMessage());
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST_BODY);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> unauthorizedException(UnauthorizedException exception) {
        logger.error("Unauthorized access attempt", exception);
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED_USER);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundException(UnauthorizedException exception) {
        logger.info("Entity not found: {}", exception.getMessage());
        return ErrorResponse.of(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_ENTITY);
    }
}
