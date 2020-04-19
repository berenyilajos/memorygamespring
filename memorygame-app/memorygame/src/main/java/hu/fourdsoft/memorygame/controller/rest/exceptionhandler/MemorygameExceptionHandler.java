package hu.fourdsoft.memorygame.controller.rest.exceptionhandler;

import com.atlassian.oai.validator.report.ValidationReport;
import com.atlassian.oai.validator.springmvc.InvalidRequestException;
import com.atlassian.oai.validator.springmvc.InvalidResponseException;
import hu.fourdsoft.memorygame.common.api.dto.ErrorResponse;
import hu.fourdsoft.memorygame.common.api.dto.SuccessType;
import hu.fourdsoft.memorygame.exception.MyApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class MemorygameExceptionHandler {

    @ExceptionHandler({MyApplicationException.class, InvalidRequestException.class, InvalidResponseException.class})
    public ResponseEntity<ErrorResponse> toResponseEntity(Exception exception) {
        ErrorResponse rrt = new ErrorResponse();
        rrt.setSuccess(SuccessType.ERROR);
        rrt.setMessage(createMessage(exception));
        log.debug(rrt.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(rrt);
    }

    private String createMessage(Exception exception) {
        if (exception instanceof InvalidRequestException) {
            return createMessage(((InvalidRequestException) exception).getValidationReport());
        } else if (exception instanceof InvalidResponseException) {
            return createMessage(((InvalidResponseException) exception).getValidationReport());
        } else {
            return exception.getMessage();
        }
    }

    private String createMessage(ValidationReport validationReport) {
        return validationReport.getMessages().stream().map(ValidationReport.Message::getMessage).collect(Collectors.toList()).toString();
    }

}
