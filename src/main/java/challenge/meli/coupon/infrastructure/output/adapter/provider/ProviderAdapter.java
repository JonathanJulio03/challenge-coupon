package challenge.meli.coupon.infrastructure.output.adapter.provider;

import static challenge.meli.coupon.commons.helper.Constants.*;

import challenge.meli.coupon.application.output.ProviderPort;
import challenge.meli.coupon.commons.exception.message.TechnicalErrorMessage;
import challenge.meli.coupon.domain.ItemModel;
import challenge.meli.coupon.domain.TokenModel;
import challenge.meli.coupon.infrastructure.output.adapter.provider.command.response.ItemsResponse;
import challenge.meli.coupon.infrastructure.output.adapter.provider.config.FeignClientUtils;
import challenge.meli.coupon.infrastructure.output.adapter.provider.config.ProviderFactory;
import challenge.meli.coupon.infrastructure.output.adapter.provider.mapper.ProviderMapper;
import challenge.meli.coupon.infrastructure.output.adapter.provider.restclient.ProviderClient;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderAdapter implements ProviderPort {

  private final ProviderClient client;
  private final ProviderMapper mapper;
  private final ProviderFactory factory;

  @Override
  public TokenModel getToken() {
    log.info("Start Adapter Service: getToken");
    return this.mapper.toDomain(
        this.client.getToken(factory.postTokenRequest()));
  }

  @Override
  public TokenModel refreshToken() {
    log.info("Start Adapter Service: refreshToken");
    return this.mapper.toDomain(
        this.client.getToken(factory.postRefreshTokenRequest()));
  }

  @Override
  public List<ItemModel> getItems(String token, String items) {
    log.info("Start Adapter Service: getItems");

    return FeignClientUtils.supplyOrThrowException(
        () -> client.getItems(
                token,
                items
            ).stream()
            .filter(response -> SUCCESS.equals(response.getCode()))
            .map(ItemsResponse::getBody)
            .filter(Objects::nonNull)
            .map(mapper::toDomain)
            .toList(),
        TechnicalErrorMessage.ERROR_ITEMS
    );
  }
}