package challenge.meli.coupon.commons.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import challenge.meli.coupon.commons.exception.message.BusinessErrorMessage;
import challenge.meli.coupon.commons.exception.message.TechnicalErrorMessage;
import challenge.meli.coupon.domain.ErrorModel;
import feign.FeignException;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @InjectMocks
  private GlobalExceptionHandler exceptionHandler;

  @Test
  @DisplayName("Test method handle business url null exception")
  void shouldHandleBusinessUrlNullException() {
    BusinessException exception = new BusinessException(BusinessErrorMessage.BAD_REQUEST_BODY);
    ResponseEntity<ErrorModel> responseEntity = exceptionHandler.handleExceptions(exception);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    assertEquals(BusinessErrorMessage.BAD_REQUEST_BODY.getCode(), Objects.requireNonNull(
        responseEntity.getBody()).getCode());
  }

  @Test
  @DisplayName("Test method handle TechnicalException")
  void shouldHandleTechnicalException() {
    String responseBody = "error-response";
    int httpStatus = 400;

    FeignException feignException = mock(FeignException.class);
    when(feignException.contentUTF8()).thenReturn(responseBody);
    when(feignException.status()).thenReturn(httpStatus);

    TechnicalException exception = new TechnicalException(feignException,
        TechnicalErrorMessage.ERROR_ITEMS);

    ResponseEntity<ErrorModel> responseEntity = exceptionHandler.handleExceptions(exception);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals(TechnicalErrorMessage.ERROR_ITEMS.getCode(), responseEntity.getBody().getCode());
  }


  @Test
  @DisplayName("Test method handle any HttpRequestMethodNotSupportedException")
  void shouldHandleHttpRequestMethodNotSupportedException() {
    Exception exception = new HttpRequestMethodNotSupportedException("");
    ResponseEntity<ErrorModel> responseEntity = exceptionHandler.handleExceptions(exception);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    assertEquals(TechnicalErrorMessage.SERVICE_NOT_FOUND.getCode(), Objects.requireNonNull(
        responseEntity.getBody()).getCode());
  }

  @Test
  @DisplayName("Test method handle any MethodArgumentNotValidException")
  void shouldHandleMethodArgumentNotValidException() {
    MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
    ResponseEntity<ErrorModel> responseEntity = exceptionHandler.handleExceptions(exception);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    assertEquals(BusinessErrorMessage.BAD_REQUEST_BODY.getCode(), Objects.requireNonNull(
        responseEntity.getBody()).getCode());
  }

  @Test
  @DisplayName("Test method handle any exception")
  void shouldHandleAnyException() {
    RuntimeException exception = new RuntimeException();
    ResponseEntity<ErrorModel> responseEntity = exceptionHandler.handleExceptions(exception);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }
}