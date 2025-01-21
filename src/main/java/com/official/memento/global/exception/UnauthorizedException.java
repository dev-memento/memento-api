package com.official.memento.global.exception;

public class UnauthorizedException extends MementoException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
