package com.tttm.birdfarmshop.Exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice //allow the dev to manage all exception instead of manage each single one
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e)
    {
        logger.error("{} : {}", e.getMessage(), e.getClass());
        return new ResponseEntity<>("Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e)
    {
        logger.error("{} : {}", HttpStatus.BAD_REQUEST.value(), "Wrong input field in the request");
        return new ResponseEntity<>("Wrong input type", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<String>handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException e)
    {
        logger.error("{} : {}", HttpStatus.BAD_REQUEST.value(), "Incorrect usage of api");
        return new ResponseEntity<>("Incorrect usage of api", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String>handleMissingServletRequestParameterException(MissingServletRequestParameterException e)
    {
        logger.error("{} : {}", HttpStatus.BAD_REQUEST.value(), "Missing parameter in request");
        return new ResponseEntity<>("Missing parameter in request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e)
    {
        logger.error("{} : {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Null Pointer Exception");
        return new ResponseEntity<>("Null Pointer Exception", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e)
    {
        logger.error("{} : {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), "IO Exception");
        return new ResponseEntity<>("IO Exception", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e)
    {
        logger.error("{} : {}", HttpStatus.FORBIDDEN.value(), "Invalid Username or Password");
        return new ResponseEntity<>("Invalid Username or Password", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e)
    {
        logger.error("{} : {}", HttpStatus.BAD_REQUEST.value(), "Duplicated field in request");
        return new ResponseEntity<>("Duplicated Field in request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleIdExistedException(CustomException e)
    {
        logger.error("{} : {}", HttpStatus.BAD_REQUEST.value(), "Id is existed");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException e)
    {
        logger.error("{} : {}", HttpStatus.FORBIDDEN.value(), "Jwt token is expired");
        return new ResponseEntity<>("Jwt token is expired", HttpStatus.FORBIDDEN);
    }
}
