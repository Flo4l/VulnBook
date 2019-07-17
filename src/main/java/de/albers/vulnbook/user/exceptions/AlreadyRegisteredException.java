package de.albers.vulnbook.user.exceptions;

public class AlreadyRegisteredException extends RuntimeException {

    public AlreadyRegisteredException() {
        super();
    }

    public AlreadyRegisteredException(String message) {
        super(message);
    }
}
