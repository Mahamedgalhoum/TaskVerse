package com.taskverse.exception;

import com.taskverse.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyExists(
            EmailAlreadyExistsException emailAlreadyExistsException){

        ApiResponse<Void> response =
                ApiResponse.error(emailAlreadyExistsException.getMessage());

        return ResponseEntity.
                status(HttpStatus.CONFLICT)
                .body(response);


    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(
            UserNotFoundException userNotFoundException){

        ApiResponse<Void> response =
                ApiResponse.error(userNotFoundException.getMessage());

        return ResponseEntity.
                status(HttpStatus.NOT_FOUND)
                .body(response);


    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(
            MethodArgumentNotValidException methodArgumentNotValidException){

        Map<String,String> errors = new HashMap<>();
        methodArgumentNotValidException
                .getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(),
                                error.getDefaultMessage()));

        ApiResponse<Void> response =
                ApiResponse.validationError("Validation Failed" ,  errors);
        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(response);

    }
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidPassword(
            InvalidPasswordException exception) {

        ApiResponse<Void> response =
                ApiResponse.error(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(
            Exception exception) {
        log.error("Unexpected error occurred: ", exception);

        ApiResponse<Void> response =
                ApiResponse.error("An unexpected error occurred");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
