package com.subhajit.sbretry.exception;

import org.springframework.retry.ExhaustedRetryException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public String handleSQLException(SQLException e){
        e.printStackTrace();
        return "SQL Exception handled!";
    }

    @ExceptionHandler(ExhaustedRetryException.class)
    public String handleExhaustRetryException(ExhaustedRetryException e){
        e.printStackTrace();
        return "Exhaust Retry Exception handled!";
    }
}
