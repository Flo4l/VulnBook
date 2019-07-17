package de.albers.vulnbook.user.exceptions;

public class IncorrectLoginException extends RuntimeException {

    public IncorrectLoginException() {
        super();
    }

    public IncorrectLoginException(String message) {
        super(message);
    }
}
