package challenge.meli.coupon.infrastructure.output.adapter.provider.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import challenge.meli.coupon.commons.properties.ProviderProperties;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.MultiValueMap;

class ProviderFactoryTest {

  private static final String CLIENT_ID = "clientId";
  private static final String CLIENT_SECRET = "clientSecret";
  private static final String CODE = "code";
  private static final String REDIRECT_URI = "redirectUri";
  private static final String REFRESH_TOKEN = "refreshToken";

  @Mock
  private ProviderProperties properties;

  @Mock
  private ProviderProperties.Credentials credentials;

  private ProviderFactory providerFactory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(properties.getCredentials()).thenReturn(credentials);
    providerFactory = new ProviderFactory(properties);
  }

  @Test
  void testPostTokenRequest() {
    when(credentials.getClientId()).thenReturn(CLIENT_ID);
    when(credentials.getSecret()).thenReturn(CLIENT_SECRET);
    when(credentials.getCode()).thenReturn(CODE);
    when(credentials.getRedirect()).thenReturn(REDIRECT_URI);

    MultiValueMap<String, String> result = providerFactory.postTokenRequest();

    assertNotNull(result);
    assertEquals(CLIENT_ID, result.getFirst("client_id"));
    assertEquals(CLIENT_SECRET, result.getFirst("client_secret"));
    assertEquals(CODE, result.getFirst("code"));
    assertEquals(REDIRECT_URI, result.getFirst("redirect_uri"));
  }

  @Test
  void testPostRefreshTokenRequest() {
    when(credentials.getClientId()).thenReturn(CLIENT_ID);
    when(credentials.getSecret()).thenReturn(CLIENT_SECRET);
    when(credentials.getCode()).thenReturn(REFRESH_TOKEN);

    MultiValueMap<String, String> result = providerFactory.postRefreshTokenRequest();

    assertNotNull(result);
    assertEquals(CLIENT_ID, result.getFirst("client_id"));
    assertEquals(CLIENT_SECRET, result.getFirst("client_secret"));
    assertEquals(REFRESH_TOKEN, result.getFirst("refresh_token"));
  }

  @Test
  void testBuildRequestBody() {
    MultiValueMap<String, String> result = providerFactory.buildRequestBody(Map.of(
        "key1", "value1",
        "key2", "value2"
    ));

    assertNotNull(result);
    assertEquals("value1", result.getFirst("key1"));
    assertEquals("value2", result.getFirst("key2"));
  }
}