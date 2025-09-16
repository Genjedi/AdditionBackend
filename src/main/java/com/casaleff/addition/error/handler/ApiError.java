package com.casaleff.addition.error.handler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError<E> {
    
    private int status;
    
    private Exception<E> exception;
    
}
