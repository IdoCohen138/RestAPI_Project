package exceptions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request){
        Map<String, List<String>> body = new HashMap<>();
        Set<String> errors = new HashSet<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        body.put("errors", new ArrayList<>(errors));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}