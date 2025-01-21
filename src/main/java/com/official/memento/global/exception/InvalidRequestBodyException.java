package com.official.memento.global.exception;

public class InvalidRequestBodyException extends MementoException {
    public InvalidRequestBodyException(ErrorCode errorCode) {
        super(errorCode);
    }
}
