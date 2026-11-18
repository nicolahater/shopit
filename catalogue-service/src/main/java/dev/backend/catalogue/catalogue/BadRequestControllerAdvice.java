package dev.backend.catalogue.catalogue;


import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
//GlobalExceptionHandler
public class BadRequestControllerAdvice {
    private final MessageSource messageSource;

//    @ExceptionHandler(BindException.class)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleBindException(
            BindException exception,
            Locale locale
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                messageSource.getMessage("errors.400.title", new Object[0], "errors.400.title", locale)
        );

//        problemDetail.setProperty("errors", exception.getAllErrors().stream()
//                .map(ObjectError::getDefaultMessage)
//                .toList());

//        Map<String, String> errors = exception.getFieldErrors().stream().collect(Collectors.toMap(
//                FieldError::getField,
//                fieldError -> fieldError.getDefaultMessage() != null
//                        ? fieldError.getDefaultMessage()
//                        : "Unknown"
//        ));

        Map<String, List<String>> errors = exception.getFieldErrors().stream().collect(Collectors.toMap(
                FieldError::getField,
                fieldError -> List.of(
                        fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "error",
                        fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString() : "")

        ));
        problemDetail.setProperty("errors", errors);

        return ResponseEntity.of(problemDetail).build();
//        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(
            NoSuchElementException exception,
            Locale locale
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                messageSource.getMessage(exception.getMessage(), new Object[0], exception.getMessage(), locale)
        );

        return ResponseEntity.of(problemDetail).build();
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(problemDetail);
    }
}
