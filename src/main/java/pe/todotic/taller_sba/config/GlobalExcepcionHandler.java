package pe.todotic.taller_sba.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@ControllerAdvice //permite interceptar las excepciones en nuestra aplicacion de forma global
public class GlobalExcepcionHandler {

    private final MessageSource messageSource;

    @Autowired
    public GlobalExcepcionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // excepcion que manejaremos
    ResponseEntity<?> handelValidationException(MethodArgumentNotValidException exception){

        Map<String,Object> errorDetail = new HashMap<>();

        errorDetail.put("title", "Error de validacion");
        errorDetail.put("code", "invalid_form");
        errorDetail.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());

        List<String> errores = new ArrayList<>();


        for(FieldError fieldError: exception.getBindingResult().getFieldErrors()){
            String message = messageSource.getMessage(fieldError, Locale.getDefault());
            errores.add(message);
        }

        errorDetail.put("errores",errores   );

        return new ResponseEntity<>(errorDetail, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    void handleEntityNotFoundException(EntityNotFoundException exeption){

    }
}
