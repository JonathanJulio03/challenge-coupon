package challenge.meli.coupon.infrastructure.output.adapter.provider.restclient.converters;

import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

  @Bean
  public OkHttpClient httpClientWithoutConnectionPool() {
    return new OkHttpClient(new okhttp3.OkHttpClient().newBuilder()
        .build());
  }

  @Bean
  public OkHttpClient defaultHttpClient() {
    return new OkHttpClient();
  }
}