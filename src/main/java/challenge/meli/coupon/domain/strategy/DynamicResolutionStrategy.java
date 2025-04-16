package challenge.meli.coupon.domain.strategy;

import java.math.BigInteger;
import org.springframework.stereotype.Component;

@Component
public class DynamicResolutionStrategy implements ResolutionStrategy {

  @Override
  public int getResolution(BigInteger budgetInCents) {
    if (budgetInCents.compareTo(BigInteger.valueOf(1_000_000)) <= 0) {
      return 1; // $0.01
    }
    if (budgetInCents.compareTo(BigInteger.valueOf(100_000_000)) <= 0) {
      return 10; // $0.10
    }
    if (budgetInCents.compareTo(BigInteger.valueOf(1_000_000_000)) <= 0) {
      return 100; // $1
    }
    if (budgetInCents.compareTo(BigInteger.valueOf(10_000_000_000L)) <= 0) {
      return 1_000; // $10
    }
    return 10_000; // $100
  }
}
