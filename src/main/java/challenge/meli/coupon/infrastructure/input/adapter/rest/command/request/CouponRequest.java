package challenge.meli.coupon.infrastructure.input.adapter.rest.command.request;

import challenge.meli.coupon.commons.helper.SelfValidating;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Tag(name = "CouponRequest", description = "Request buy coupon")
public class CouponRequest extends SelfValidating<CouponRequest> {

    @Schema(description = "Id items")
    @NotNull(message = "item_ids.null")
    @JsonProperty("item_ids")
    private List<@NotBlank(message = "item_ids.blank") String> itemIds;

    @NotNull(message = "amount.null")
    @DecimalMin(value = "0.01", message = "amount.min")
    @Schema(description = "Amount")
    private Double amount;

    @JsonCreator
    public CouponRequest(List<String> itemIds, Double amount) {
        this.itemIds = itemIds;
        this.amount = amount;
        this.validateSelf();
    }
}
