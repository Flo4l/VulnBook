package de.albers.vulnbook.post.exceptions;

public class AlreadyLikedException extends RuntimeException {

    public AlreadyLikedException() {
        super();
    }

    public AlreadyLikedException(String message) {
        super(message);
    }
}
