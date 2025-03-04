package com.official.memento.global.exception;

public class ExpiredTokenException extends MementoException {
  public ExpiredTokenException(ErrorCode errorCode) { super(errorCode); }
}
