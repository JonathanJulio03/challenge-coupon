package challenge.meli.coupon.commons.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechnicalErrorMessage {
  SERVICE_NOT_FOUND("CCT0001", "Service not found"),
  ERROR_ITEMS("CCT0002", "Error get items");

  private final String code;
  private final String message;
}
