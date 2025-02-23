package com.official.memento.global.exception;

public class InvalidJwtTokenException extends MementoException {
  public InvalidJwtTokenException(ErrorCode errorCode) { super(errorCode); }
}
