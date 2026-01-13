package com.official.memento.global.handler;

import com.official.memento.alarm.service.command.AlarmExceptionCommand;
import com.official.memento.alarm.service.usecase.AlarmSendUseCase;
import com.official.memento.global.dto.ErrorResponse;
import com.official.memento.global.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.DecodingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final AlarmSendUseCase alarmSendUseCase;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(final AlarmSendUseCase alarmSendUseCase) {
        this.alarmSendUseCase = alarmSendUseCase;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> noResourceFoundException(final NoResourceFoundException exception) {
        logger.error("No resource found: ", exception);
        return ErrorResponse.of(HttpStatus.NOT_FOUND, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> unauthorizedException(final UnauthorizedException exception) {
        logger.error("Unauthorized access attempt", exception);
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED_USER);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundException(final EntityNotFoundException exception) {
        logger.error("Entity not found:", exception);
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_ENTITY);
    }

    @ExceptionHandler(ClaudeException.class)
    public ResponseEntity<ErrorResponse> claudeException(final ClaudeException exception) {
        logger.error("Claude exception occurred", exception);
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.CLAUDE_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentTypeMismatchException(
            final MethodArgumentTypeMismatchException exception) {
        logger.error("Invalid request:", exception);
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_ARGUMENT_TYPE);
    }

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<ErrorResponse> DecodingException(final DecodingException exception) {
        logger.error("Invalid request:", exception);
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.DECODING_FAILURE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> HttpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        logger.error("Invalid request:", exception);
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_JSON_FORMAT);
    }

    @ExceptionHandler(InvalidAiRequestException.class)
    public ResponseEntity<ErrorResponse> invalidAiRequestException(final InvalidAiRequestException exception) {
        logger.error("Invalid AI request:", exception);
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_AI_PRIORITIZATION_REQUEST);
    }

    @ExceptionHandler(MementoException.class)
    public ResponseEntity<ErrorResponse> mementoException(final MementoException exception) {
        logger.error("MementoException", exception);
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeException(final RuntimeException exception, final HttpServletRequest request) {
        logger.error("Runtime exception occurred ", exception);
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<ErrorResponse> invalidRequestBodyException(final InvalidRequestBodyException exception) {
        logger.warn("Invalid request body: {}", exception.getMessage());
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST_BODY);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ErrorResponse> expiredJwtException(final ExpiredTokenException exception) {
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, ErrorCode.EXPIRED_TOKEN);
    }

    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<ErrorResponse> invalidJwtTokenException(final InvalidJwtTokenException exception) {
        logger.error("Unauthorized access attempt", exception.getMessage());
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.EXPECTED_TOKEN_ERROR);
    }

    @ExceptionHandler(UnexpectedTokenException.class)
    public ResponseEntity<ErrorResponse> unexpectedTokenException(final UnexpectedTokenException exception) {
        logger.error("Unauthorized access attempt", exception.getMessage());
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.UNEXPECTED_TOKEN_ERROR);
    }

    @ExceptionHandler(DataBaseIntegrityException.class)
    public ResponseEntity<ErrorResponse> dataBaseIntegrityException(final DataBaseIntegrityException exception) {
        logger.warn("Database exception occurred", exception.getMessage());
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.UNEXPECTED_TOKEN_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(final Exception exception) {
        logger.error("Unhandled exception occurred ", exception);
        alarmSendUseCase.sendException(new AlarmExceptionCommand(exception));
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR);
    }

}
