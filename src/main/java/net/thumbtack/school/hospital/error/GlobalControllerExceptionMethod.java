package net.thumbtack.school.hospital.error;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalControllerExceptionMethod extends ResponseEntityExceptionHandler {


//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Object> handleRuntimeException(
//            RuntimeException ex, WebRequest request) throws ServerException {
//
//        String message = ex.getCause().toString();
//        throw new ServerException(new MyError(ServerErrorCode.DATABASE_ERROR, Field.UNKNOWN, message));
//    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Object> handleServerException(
            ServerException ex, WebRequest request) {

        return new ResponseEntity<>(ex.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Object> handlePersistenceException(
            PersistenceException ex, WebRequest request) throws ServerException {
        throw new ServerException(new MyError(ServerErrorCode.DATABASE_ERROR, Field.UNKNOWN));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        List<MyError> errors = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(new MyError(fieldError.getCode(), fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue().toString()));
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(new MyError(ServerErrorCode.INVALID_JSON, Field.UNKNOWN, error.getDefaultMessage(), ""));
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        MyError myError = new MyError(ServerErrorCode.INVALID_JSON, Field.UNKNOWN);
        List<MyError> list = new ArrayList<>();
        list.add(myError);
        return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MismatchedInputException.class)
//    public ResponseEntity<Object> handleMismatchedInputException(
//            MismatchedInputException ex, WebRequest request) throws ServerException {
//        String message = ex.getTargetType().toString();
//        throw new ServerException(new MyError(ServerErrorCode.DATABASE_ERROR, Field.UNKNOWN, message));
//    }

//    @ExceptionHandler(HttpMessageConversionException.class)
//    public ResponseEntity<Object> handleHttpMessageConversionException(
//            HttpMessageConversionException ex, WebRequest request) throws ServerException {
//        String message = ex.getMessage();
//        throw new ServerException(new MyError(ServerErrorCode.DATABASE_ERROR, Field.UNKNOWN, message));
//    }


//    @ExceptionHandler(JsonParseException.class)
//    public ResponseEntity<Object> handleJsonParseException(
//            JsonParseException ex, WebRequest request) throws ServerException {
//        String message = ex.getMessage();
//        throw new ServerException(new MyError(ServerErrorCode.DATABASE_ERROR, Field.UNKNOWN, message));
//    }


}
