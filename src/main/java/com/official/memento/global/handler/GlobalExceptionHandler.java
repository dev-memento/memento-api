package com.official.memento.global.handler;

import com.official.memento.alarm.service.command.AlarmExceptionCommand;
import com.official.memento.alarm.service.command.AlarmSendUseCase;
import com.official.memento.global.dto.ErrorResponse;
import com.official.memento.global.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final AlarmSendUseCase alarmSendUseCase;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(AlarmSendUseCase alarmSendUseCase) {
        this.alarmSendUseCase = alarmSendUseCase;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception exception) {
        logger.error("Unhandled exception occurred ", exception);
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MementoException.class)
    public ResponseEntity<ErrorResponse> mementoException(MementoException exception) {
        logger.error("MementoException", exception);
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<ErrorResponse> invalidRequestBodyException(InvalidRequestBodyException exception) {
        logger.warn("Invalid request body: {}", exception.getMessage());
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST_BODY);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> unauthorizedException(UnauthorizedException exception) {
        logger.error("Unauthorized access attempt", exception);
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED_USER);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundException(EntityNotFoundException exception) {
        logger.error("Entity not found:", exception);
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_ENTITY);
    }

    @ExceptionHandler(NullPointException.class)
    public ResponseEntity<ErrorResponse> nullPointException(NullPointException exception) {
        logger.error("Invalid request:", exception);
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_ENTITY);
    }
}
