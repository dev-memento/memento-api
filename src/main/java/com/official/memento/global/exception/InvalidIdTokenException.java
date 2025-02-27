package com.official.memento.global.exception;

public class InvalidIdTokenException extends MementoException {
  public InvalidIdTokenException(ErrorCode errorCode) { super(errorCode); }
}
