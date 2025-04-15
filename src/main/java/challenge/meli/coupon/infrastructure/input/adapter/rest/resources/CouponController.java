package challenge.meli.coupon.infrastructure.input.adapter.rest.resources;

import static org.springframework.http.HttpStatus.OK;

import challenge.meli.coupon.application.input.CouponUseCase;
import challenge.meli.coupon.domain.ErrorModel;
import challenge.meli.coupon.infrastructure.input.adapter.rest.command.request.CouponRequest;
import challenge.meli.coupon.infrastructure.input.adapter.rest.command.response.CouponResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Coupon", description = "Service to generate purchases from a coupon")
@RequestMapping("/coupon")
public class CouponController {

  private final CouponUseCase useCase;

  @ApiResponse(responseCode = "200", description = "Request Success", content = {
      @Content(mediaType = "application/json",
          schema = @Schema(implementation = CouponResponse.class))})
  @ApiResponse(responseCode = "400", description = "Bad Request", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorModel.class))})
  @ApiResponse(responseCode = "404", description = "Not Found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorModel.class))})
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<CouponResponse> coupon(@Valid @RequestBody CouponRequest couponRequest) {
    return new ResponseEntity<>(useCase.coupon(couponRequest), OK);
  }
}
