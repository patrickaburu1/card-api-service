package com.card.cardapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedExceptionNotFound extends RuntimeException {


    //custom CustomExceptionNotFound class
    public UnAuthorizedExceptionNotFound( String message) {
        super(message);
    }
}


