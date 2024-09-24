package com.dev.identity.exception;

import com.dev.commons.exception.CustomException;
import com.dev.commons.response.DataResponse;
import com.dev.commons.response.ErrorModel;
import com.dev.commons.response.ResultModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleException(CustomException ex) {
        DataResponse dataResponse = DataResponse.builder()
                .status(false)
                .result(ResultModel.builder()
                        .content(ex.getErrorModel())
                        .build())
                .build();
        HttpStatus status = HttpStatus.resolve(ex.getErrorModel().getCode());

        return new ResponseEntity<>(dataResponse, status != null ? status : HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a 400 Bad Request response.
     *
     * @param ex The MethodArgumentNotValidException instance.
     * @return ResponseEntity containing a Map of field errors with their corresponding error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleBindingException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorModel errorModel = new ErrorModel(500, errors);

        DataResponse dataResponse = DataResponse.builder()
                .status(false)
                .result(ResultModel.builder()
                        .content(errorModel)
                        .build())
                .build();

        return new ResponseEntity<>(dataResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleException(Exception ex) {
        ErrorModel errorModel = new ErrorModel(500, ex.getMessage());

        DataResponse dataResponse = DataResponse.builder()
                .status(false)
                .result(ResultModel.builder()
                        .content(errorModel)
                        .build())
                .build();

        return new ResponseEntity<>(dataResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
