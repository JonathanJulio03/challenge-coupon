package challenge.meli.coupon.application.input;

import challenge.meli.coupon.infrastructure.input.adapter.rest.command.request.CouponRequest;
import challenge.meli.coupon.infrastructure.input.adapter.rest.command.response.CouponResponse;

public interface CouponUseCase {
    CouponResponse coupon(CouponRequest couponRequest);
}
