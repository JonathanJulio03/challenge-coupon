package challenge.meli.coupon.infrastructure.output.adapter.provider.mapper;

import challenge.meli.coupon.domain.ItemModel;
import challenge.meli.coupon.domain.TokenModel;
import challenge.meli.coupon.infrastructure.output.adapter.provider.command.response.ItemsResponse.ItemResponse;
import challenge.meli.coupon.infrastructure.output.adapter.provider.command.response.TokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProviderMapper {
    TokenModel toDomain(TokenResponse source);
    ItemModel toDomain(ItemResponse source);
}
