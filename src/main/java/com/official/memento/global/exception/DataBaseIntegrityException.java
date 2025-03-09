package com.official.memento.global.exception;

public class DataBaseIntegrityException extends MementoException{
    public DataBaseIntegrityException(final ErrorCode errorCode){
        super(errorCode);
    }
}
