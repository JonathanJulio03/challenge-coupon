package challenge.meli.coupon.commons.exception;

import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException {

    private final String message;

    public ErrorException(String message) {
        this.message = message;
    }
}
