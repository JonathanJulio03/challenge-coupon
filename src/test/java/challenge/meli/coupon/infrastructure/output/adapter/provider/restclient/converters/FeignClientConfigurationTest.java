package challenge.meli.coupon.infrastructure.output.adapter.provider.restclient.converters;

import static org.junit.jupiter.api.Assertions.*;

import feign.okhttp.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeignClientConfigurationTest {

  @Test
  void testClientBeanCreation() {
    FeignClientConfiguration feignClientConfiguration = new FeignClientConfiguration();
    OkHttpClient client = feignClientConfiguration.defaultHttpClient();
    assertNotNull(client);
  }

  @Test
  void testClientWithoutConnectionPool() {
    FeignClientConfiguration feignClientConfiguration = new FeignClientConfiguration();
    OkHttpClient client = feignClientConfiguration.httpClientWithoutConnectionPool();
    assertNotNull(client);
  }

}