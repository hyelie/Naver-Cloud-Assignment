package assignment.simpleboard.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { DuplicateKeyException.class })
    protected ResponseEntity<Object> handleDuplicatedKeyException() {
        log.error("handleDataException throws Exceptions : {}", ErrorCode.PARAMETER_NOT_VALID);
        return ErrorResponse.toResponseEntity(ErrorCode.PARAMETER_NOT_VALID);
    }

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<Object> handleCustomException(CustomException e) {
        log.error("custom exception throws Exceptions : {}", e.getErrorCode().getMessage());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("handleHttpMessageNotReadable throws Exceptions : {}", ErrorCode.PARAMETER_NOT_VALID);
        return new ResponseEntity<Object>(ErrorResponse.toResponseObject(ErrorCode.PARAMETER_NOT_VALID), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("handleValidationViolateException throws Exceptions : {}", ErrorCode.PARAMETER_NOT_VALID);
        System.out.println(request);;
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<Object>(ErrorResponse.toResponseObject(ErrorCode.PARAMETER_NOT_VALID, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("handleNoHandlerFoundException throws Exceptions : {}", ErrorCode.ENDPOINT_NOT_FOUND);
        String errorMessage = ex.getLocalizedMessage();
        return new ResponseEntity<Object>(ErrorResponse.toResponseObject(ErrorCode.ENDPOINT_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("handleNoResourceFoundException throws Exceptions : {}", ErrorCode.ENDPOINT_NOT_FOUND);
        String errorMessage = ex.getLocalizedMessage();
        return new ResponseEntity<Object>(ErrorResponse.toResponseObject(ErrorCode.ENDPOINT_NOT_FOUND), HttpStatus.NOT_FOUND);
    }
}
