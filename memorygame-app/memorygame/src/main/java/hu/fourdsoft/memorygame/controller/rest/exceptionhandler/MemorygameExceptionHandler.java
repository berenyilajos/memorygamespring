package hu.fourdsoft.memorygame.controller.rest.exceptionhandler;

import com.atlassian.oai.validator.springmvc.InvalidRequestException;
import com.atlassian.oai.validator.springmvc.InvalidResponseException;
import hu.fourdsoft.mamorygame.common.api.dto.ErrorResponse;
import hu.fourdsoft.mamorygame.common.api.dto.SuccessType;
import hu.fourdsoft.memorygame.exception.MyApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class MemorygameExceptionHandler {

    @ExceptionHandler({MyApplicationException.class, InvalidRequestException.class, InvalidResponseException.class})
    public ResponseEntity<ErrorResponse> toResponseEntity(Exception exception) {
        ErrorResponse rrt = new ErrorResponse();
        rrt.setSuccess(SuccessType.ERROR);
        rrt.setMessage(exception.getMessage());
        log.debug(rrt.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(rrt);
    }

}
