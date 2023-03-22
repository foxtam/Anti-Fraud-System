package net.foxtam.antifraudsystem.controller;

import net.foxtam.antifraudsystem.exceptions.WrongFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(WrongFormatException.class)
    public ResponseEntity<?> handleWrongFormatException(WrongFormatException e, WebRequest request) {
        CustomErrorMessage body =
                new CustomErrorMessage(
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now(),
                        e.getMessage(),
                        request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

record CustomErrorMessage(int statusCode, LocalDateTime timestamp, String message, String description) {
}
