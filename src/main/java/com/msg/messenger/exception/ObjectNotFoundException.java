package com.msg.messenger.exception;

public class ObjectNotFoundException
    extends ValidatorException {

    public ObjectNotFoundException(final String fieldName) {
        super("not_found", fieldName);
    }
}
