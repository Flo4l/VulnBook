package de.albers.vulnbook.post.exceptions;

public class PostEmptyException extends RuntimeException {

    public PostEmptyException() {
        super();
    }

    public PostEmptyException(String message) {
        super(message);
    }
}
