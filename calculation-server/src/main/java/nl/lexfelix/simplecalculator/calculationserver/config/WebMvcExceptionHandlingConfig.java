package nl.lexfelix.simplecalculator.calculationserver.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class WebMvcExceptionHandlingConfig extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoSuchElementException.class})
    public void handleNoSuchElementExceptionConflict(){
    }

    @ExceptionHandler({ArithmeticException.class})
    protected ResponseEntity<Object>  handleArithmeticExceptionConflict(ArithmeticException ex){
        return ResponseEntity.badRequest().body("One of the calculations you provided is not valid because: " +ex.getMessage());
    }

}
