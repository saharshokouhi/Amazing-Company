package com.tradeshift.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NodeMoveException extends RuntimeException{
    public NodeMoveException(String message) {
        super(message);
    }
}
