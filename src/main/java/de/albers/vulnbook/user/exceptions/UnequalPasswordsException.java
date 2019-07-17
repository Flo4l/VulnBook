package de.albers.vulnbook.user.exceptions;

public class UnequalPasswordsException extends RuntimeException {

    public UnequalPasswordsException() {
        super();
    }

    public UnequalPasswordsException(String message) {
        super(message);
    }
}
