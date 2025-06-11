package br.com.realtour.exception;

public class InvalidCredientialsException extends RuntimeException {
    public InvalidCredientialsException(String message) {
        super(message);
    }
}
