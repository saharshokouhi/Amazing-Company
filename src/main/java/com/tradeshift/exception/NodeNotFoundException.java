package com.tradeshift.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NodeNotFoundException extends RuntimeException{
    public NodeNotFoundException(String message) {
        super(message);
    }
}
