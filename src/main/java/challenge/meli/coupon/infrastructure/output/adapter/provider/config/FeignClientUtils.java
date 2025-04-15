package challenge.meli.coupon.infrastructure.output.adapter.provider.config;

import static challenge.meli.coupon.commons.helper.Constants.BRACKETS;

import challenge.meli.coupon.commons.exception.ErrorException;
import challenge.meli.coupon.commons.exception.TechnicalException;
import challenge.meli.coupon.commons.exception.message.TechnicalErrorMessage;
import feign.FeignException.FeignClientException;
import feign.FeignException.FeignServerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Supplier;

@Slf4j
public class FeignClientUtils {

  private FeignClientUtils() {}

  public static <T> T supplyOrThrowException(final Supplier<T> supplier,
      final TechnicalErrorMessage technicalErrorMessage) {
    try {
      return supplier.get();
    } catch (FeignClientException | FeignServerException e) {
      log.error(BRACKETS, technicalErrorMessage.getMessage());
      throw new TechnicalException(e, technicalErrorMessage);
    } catch (final Exception e) {
      log.error(BRACKETS, e.getMessage());
      throw new ErrorException(e.getMessage());
    }
  }
}
