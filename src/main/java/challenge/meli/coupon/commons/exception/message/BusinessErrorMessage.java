package challenge.meli.coupon.commons.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {
  BAD_REQUEST_BODY("CCB0001", "Error in body request"),
  ERROR_GET_ITEMS("CCB0002", "Don't get items");;

  private final String code;
  private final String message;
}
