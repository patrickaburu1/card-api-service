package com.card.cardapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomExceptionNotFound extends RuntimeException {


    //custom CustomExceptionNotFound class
    public CustomExceptionNotFound(String message) {
        super(message);
    }
}


