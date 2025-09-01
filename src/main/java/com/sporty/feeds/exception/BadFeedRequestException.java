package com.sporty.feeds.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadFeedRequestException extends RuntimeException {
    public BadFeedRequestException(String message) {
        super(message);
    }
}
