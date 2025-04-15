package challenge.meli.coupon.domain.strategy;

import static challenge.meli.coupon.commons.helper.Constants.*;

import challenge.meli.coupon.application.output.ProviderPort;
import challenge.meli.coupon.application.service.CacheService;
import challenge.meli.coupon.commons.exception.ErrorException;
import challenge.meli.coupon.domain.ItemModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemFetcher {

  private final ProviderPort output;
  private final CacheService cacheService;

  public List<ItemModel> fetch(List<String> itemIds) {
    List<ItemModel> result = new ArrayList<>();
    List<String> toFetchFromApi = new ArrayList<>();

    for (String id : itemIds) {
      cacheService.getItem(id).ifPresentOrElse(
          result::add,
          () -> toFetchFromApi.add(id)
      );
    }

    if (!toFetchFromApi.isEmpty()) {
      List<ItemModel> fetched = IntStream.range(0, (toFetchFromApi.size() + 19) / 20)
          .mapToObj(
              i -> toFetchFromApi.subList(i * 20, Math.min((i + 1) * 20, toFetchFromApi.size())))
          .map(batch -> output.getItems(
              String.format(AUTHORIZATION_BEARER_TOKEN_FORMAT, getToken()),
              String.join(COMMA, batch)))
          .flatMap(List::stream)
          .filter(Objects::nonNull)
          .toList();

      fetched.forEach(item -> cacheService.saveItem(item.getId(), item));
      result.addAll(fetched);
    }

    return result;
  }

  private String getToken() {
    return cacheService.getToken()
        .orElseThrow(() -> new ErrorException(ERROR_NOT_FOUND_TOKEN))
        .getAccessToken();
  }
}
