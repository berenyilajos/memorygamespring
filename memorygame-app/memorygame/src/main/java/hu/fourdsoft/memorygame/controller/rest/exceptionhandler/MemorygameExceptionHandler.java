package hu.fourdsoft.memorygame.controller.rest.exceptionhandler;

import hu.fourdsoft.memorygame.exception.MyApplicationException;
import hu.fourdsoft.xsdpojo.common.common.SuccessType;
import hu.fourdsoft.xsdpojo.pojo.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class MemorygameExceptionHandler {

    @ExceptionHandler({MyApplicationException.class})
    public ResponseEntity<ResultResponse> toResponseEntity(MyApplicationException exception) {
        ResultResponse rrt = new ResultResponse();
        rrt.setSuccess(SuccessType.ERROR);
        rrt.setMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(rrt);
    }

}
