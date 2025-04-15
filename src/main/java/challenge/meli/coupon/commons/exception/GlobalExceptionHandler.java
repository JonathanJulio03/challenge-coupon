package challenge.meli.coupon.commons.exception;

import challenge.meli.coupon.commons.exception.message.BusinessErrorMessage;
import challenge.meli.coupon.commons.exception.message.TechnicalErrorMessage;
import challenge.meli.coupon.domain.ErrorModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

  private static ErrorModel getErrorResponses(BusinessException businessException) {
    return ErrorModel.builder().code(businessException.getBusinessErrorMessage().getCode())
        .message(businessException.getBusinessErrorMessage().getMessage()).build();
  }

  private static ErrorModel getErrorResponses(TechnicalException technicalException) {
    try {
      return new ObjectMapper().readValue(technicalException.getResponseBody(), ErrorModel.class);
    } catch (Exception e) {
      return ErrorModel.builder().code(technicalException.getTechnicalErrorMessage().getCode())
          .message(technicalException.getTechnicalErrorMessage().getMessage()).build();
    }
  }

  private static ErrorModel getErrorResponses(Exception ex) {
    try {
      throw ex;
    } catch (final HttpRequestMethodNotSupportedException e) {
      return ErrorModel.builder().code(TechnicalErrorMessage.SERVICE_NOT_FOUND.getCode())
          .message(TechnicalErrorMessage.SERVICE_NOT_FOUND.getMessage()).build();
    } catch (final MethodArgumentNotValidException e) {
      return ErrorModel.builder().code(BusinessErrorMessage.BAD_REQUEST_BODY.getCode())
          .message(BusinessErrorMessage.BAD_REQUEST_BODY.getMessage()).build();
    } catch (Exception e) {
      String message = !Objects.isNull(e.getMessage()) ? e.getMessage() : HttpStatus.BAD_REQUEST.name();
      log.error(message);
      return ErrorModel.builder().code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
          .message(message).build();
    }
  }

  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorModel> handleExceptions(BusinessException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(getErrorResponses(ex), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TechnicalException.class)
  public ResponseEntity<ErrorModel> handleExceptions(TechnicalException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(getErrorResponses(ex), ex.getStatus());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolationException(
      ConstraintViolationException ex) {
    return new ResponseEntity<>(getErrorResponses(ex), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorModel> handleExceptions(Exception ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(getErrorResponses(ex), HttpStatus.BAD_REQUEST);
  }
}