package com.casaleff.addition.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException{
    
    HttpStatus status;

    public BaseException(ErrorType errorType, String message){
        super(prepareErrorMessage(errorType,message));
        this.status = errorType.getHttpStatus();
    }
    
    private static String prepareErrorMessage(ErrorType errorType, String message){
        StringBuilder builder = new StringBuilder();
        builder.append(errorType.getMessage());
        if(message!=null)
            builder.append(" : " + message );
        builder.append(" : " + errorType.getCode()); 
        return builder.toString();
    }
    
    
}
