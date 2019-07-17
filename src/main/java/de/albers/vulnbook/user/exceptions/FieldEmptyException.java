package de.albers.vulnbook.user.exceptions;

public class FieldEmptyException extends RuntimeException {

    public FieldEmptyException() {
        super();
    }

    public FieldEmptyException(String message) {
        super(message);
    }
}
