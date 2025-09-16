package com.casaleff.addition.error.handler;

import com.casaleff.addition.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<ApiError> handleBaseException(BaseException exception, WebRequest request){
        return ResponseEntity.status(exception.getStatus()).body(createError(exception.getMessage(), request, exception.getStatus()));
    }

    public <E> ApiError<E> createError(E message, WebRequest request, HttpStatus status){
        ApiError<E> apiError = new ApiError<>();
        apiError.setStatus(status.value());
        
        Exception<E> exception = new Exception<>();
        exception.setCreationDate(new Date());
        exception.setHostName(getHostName());
        exception.setPath(request.getDescription(false));
        exception.setMessage(message);

        apiError.setException(exception);
        return apiError;
    }

    private String getHostName(){
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            System.out.println("Error getting the hostname" + e.getMessage());
        }
        return null;
    }    
}
