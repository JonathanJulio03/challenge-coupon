package challenge.meli.coupon.domain.strategy;

import java.math.BigInteger;

public interface ResolutionStrategy {
    int getResolution(BigInteger budgetInCents);
}
