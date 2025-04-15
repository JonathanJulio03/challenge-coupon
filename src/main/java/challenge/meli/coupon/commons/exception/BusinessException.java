package challenge.meli.coupon.commons.exception;

import challenge.meli.coupon.commons.exception.message.BusinessErrorMessage;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

  final BusinessErrorMessage businessErrorMessage;

  public BusinessException(final BusinessErrorMessage businessErrorMessage) {
    this.businessErrorMessage = businessErrorMessage;
  }
}