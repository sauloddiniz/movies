package com.movies.config.exception;

import com.movies.model.DTO.Error.ErrorFieldDTO;
import com.movies.model.DTO.Error.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class DefaultException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ObjectPresentException.class)
    public ResponseEntity<Object> moviePresentException(ObjectPresentException exception, WebRequest request){
        String path = getPath((ServletWebRequest) request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponseDTO
                .builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.CONFLICT.getReasonPhrase())
                        .path(path)
                .build());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        List<ErrorFieldDTO> listError = ex.getFieldErrors()
                .stream().map(ErrorFieldDTO::converter)
                .collect(Collectors.toList());

        String path = getPath((ServletWebRequest) request);

        return ResponseEntity.badRequest().body(
                ErrorResponseDTO
                .builder()
                        .errors(listError).status(status.getReasonPhrase()).path(path)
                .build());
    }

    private static String getPath(ServletWebRequest request) {
        return request.getRequest().getRequestURI();
    }
}
