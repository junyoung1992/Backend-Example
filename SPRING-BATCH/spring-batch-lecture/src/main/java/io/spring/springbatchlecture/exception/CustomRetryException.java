package io.spring.springbatchlecture.exception;

public class CustomRetryException extends Exception {

    public CustomRetryException() {
        super();
    }

    public CustomRetryException(String s) {
        super(s);
    }
}
