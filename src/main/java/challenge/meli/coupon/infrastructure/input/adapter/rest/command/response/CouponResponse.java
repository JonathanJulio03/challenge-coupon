package challenge.meli.coupon.infrastructure.input.adapter.rest.command.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Tag(name = "CouponResponse", description = "Response coupon")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponResponse {

    @Schema(description = "Id items")
    @NotNull(message = "item_ids.null")
    @JsonProperty("item_ids")
    List<String> itemIds;

    @Schema(description = "Total")
    Double total;
}
