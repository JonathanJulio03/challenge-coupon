package challenge.meli.coupon.commons.exception;

import challenge.meli.coupon.commons.exception.message.TechnicalErrorMessage;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TechnicalException extends RuntimeException {

  final TechnicalErrorMessage technicalErrorMessage;
  final String responseBody;
  @NonNull
  final HttpStatus status;

  public TechnicalException(final FeignException cause,
      final TechnicalErrorMessage technicalErrorMessage) {
    super(cause);
    this.technicalErrorMessage = technicalErrorMessage;
    this.responseBody = cause.contentUTF8();
    this.status = HttpStatus.valueOf(cause.status());
  }
}