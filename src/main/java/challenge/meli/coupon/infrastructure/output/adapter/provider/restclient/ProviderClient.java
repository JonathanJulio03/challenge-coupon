package challenge.meli.coupon.infrastructure.output.adapter.provider.restclient;

import static challenge.meli.coupon.commons.helper.Constants.AUTHORIZATION_HEADER;

import challenge.meli.coupon.infrastructure.output.adapter.provider.command.response.ItemsResponse;
import challenge.meli.coupon.infrastructure.output.adapter.provider.command.response.TokenResponse;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
@EnableRetry
@FeignClient(name = "${client.api.name:provider}", url = "${client.api.host}")
public interface ProviderClient {

  @PostMapping(value = "${client.api.token}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  TokenResponse getToken(@RequestBody MultiValueMap<String, String> formParams);

  @Retry(name = "itemsRetry")
  @GetMapping("${client.api.items}")
  List<ItemsResponse> getItems(
      @RequestHeader(name = AUTHORIZATION_HEADER) String authorization,
      @RequestParam(name = "items") String items);
}
