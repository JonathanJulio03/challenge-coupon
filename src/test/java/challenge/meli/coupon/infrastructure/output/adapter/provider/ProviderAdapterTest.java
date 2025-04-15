package challenge.meli.coupon.infrastructure.output.adapter.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import challenge.meli.coupon.commons.exception.ErrorException;
import challenge.meli.coupon.domain.ItemModel;
import challenge.meli.coupon.domain.TokenModel;
import challenge.meli.coupon.infrastructure.output.adapter.provider.command.response.ItemsResponse;
import challenge.meli.coupon.infrastructure.output.adapter.provider.command.response.ItemsResponse.ItemResponse;
import challenge.meli.coupon.infrastructure.output.adapter.provider.command.response.TokenResponse;
import challenge.meli.coupon.infrastructure.output.adapter.provider.config.ProviderFactory;
import challenge.meli.coupon.infrastructure.output.adapter.provider.mapper.ProviderMapper;
import challenge.meli.coupon.infrastructure.output.adapter.provider.restclient.ProviderClient;
import feign.FeignException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@ExtendWith(MockitoExtension.class)
class ProviderAdapterTest {

  private static final String MOCK_TOKEN = "mockToken";
  private static final String MOCK_REFRESH_TOKEN = "mockRefreshToken";
  private static final String MOCK_ITEMS = "mockItems";
  private static final String MOCK_ID = "mockId";
  private static final double MOCK_PRICE = 100.0;
  private static final int MOCK_EXPIRES_IN = 3600;

  @InjectMocks
  private ProviderAdapter providerAdapter;

  @Mock
  private ProviderClient client;

  @Mock
  private ProviderFactory factory;

  @Spy
  private ProviderMapper mapper = Mappers.getMapper(ProviderMapper.class);

  @Test
  void testGetToken() {
    TokenResponse tokenResponse = new TokenResponse();
    tokenResponse.setAccessToken(MOCK_TOKEN);
    tokenResponse.setExpiresIn(MOCK_EXPIRES_IN);

    MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
    when(factory.postTokenRequest()).thenReturn(request);
    when(client.getToken(request)).thenReturn(tokenResponse);
    TokenModel expected = new TokenModel(MOCK_EXPIRES_IN, MOCK_TOKEN);
    when(mapper.toDomain(tokenResponse)).thenReturn(expected);

    TokenModel result = providerAdapter.getToken();

    assertEquals(expected, result);
    verify(client).getToken(request);
    verify(mapper).toDomain(tokenResponse);
  }

  @Test
  void testRefreshToken() {
    TokenResponse tokenResponse = new TokenResponse();
    tokenResponse.setAccessToken(MOCK_REFRESH_TOKEN);
    tokenResponse.setExpiresIn(MOCK_EXPIRES_IN);

    MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
    when(factory.postRefreshTokenRequest()).thenReturn(request);
    when(client.getToken(request)).thenReturn(tokenResponse);
    TokenModel expected = new TokenModel(MOCK_EXPIRES_IN, MOCK_REFRESH_TOKEN);
    when(mapper.toDomain(tokenResponse)).thenReturn(expected);

    TokenModel result = providerAdapter.refreshToken();

    assertEquals(expected, result);
    verify(client).getToken(request);
    verify(mapper).toDomain(tokenResponse);
  }

  @Test
  void testGetItems() {
    ItemResponse itemResponse = new ItemResponse();
    itemResponse.setId(MOCK_ID);
    itemResponse.setPrice(MOCK_PRICE);
    List<ItemsResponse> itemsResponseList = Collections.singletonList(
        new ItemsResponse("200", itemResponse));

    when(client.getItems(MOCK_TOKEN, MOCK_ITEMS)).thenReturn(itemsResponseList);

    ItemModel itemModel = new ItemModel(MOCK_ID, MOCK_PRICE);
    when(mapper.toDomain(itemResponse)).thenReturn(itemModel);

    List<ItemModel> result = providerAdapter.getItems(MOCK_TOKEN, MOCK_ITEMS);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(itemModel, result.get(0));
    verify(client).getItems(MOCK_TOKEN, MOCK_ITEMS);
    verify(mapper).toDomain(itemResponse);
  }

  @Test
  void testGetItemsWithException() {
    doThrow(FeignException.class).when(client).getItems(MOCK_TOKEN, MOCK_ITEMS);
    assertThrows(ErrorException.class, () -> providerAdapter.getItems(MOCK_TOKEN, MOCK_ITEMS));
  }
}
