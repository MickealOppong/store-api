package com.store.api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleConstraintViolationException(MethodArgumentNotValidException exception,
                                                                   HttpServletRequest request) {
        Map<String,String> err = new HashMap<>();
        try {
            Map<String,String>errorMap=new HashMap<>();
            exception.getBindingResult().getFieldErrors().forEach(error->
            {
                errorMap.put(error.getField(),error.getDefaultMessage());
            });
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonList(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
