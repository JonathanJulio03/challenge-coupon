package challenge.meli.coupon.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import challenge.meli.coupon.application.output.ProviderPort;
import challenge.meli.coupon.domain.TokenModel;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

  @InjectMocks
  private TokenService tokenService;

  @Mock
  private CacheService cacheService;

  @Mock
  private ProviderPort providerPort;

  @Test
  void getFreshToken_whenTokenInCache_shouldReturnToken() {
    TokenModel token = new TokenModel(3600, "cached-token");

    when(cacheService.getToken()).thenReturn(Optional.of(token));

    Optional<TokenModel> result = invokePrivateGetFreshToken();

    assertTrue(result.isPresent());
    assertEquals("cached-token", result.get().getAccessToken());
    verify(cacheService).getToken();
    verifyNoMoreInteractions(providerPort);
  }

  @Test
  void getFreshToken_whenCacheEmpty_butGetTokenWorks_shouldReturnToken() {
    TokenModel token = new TokenModel(3600, "provider-token");

    when(cacheService.getToken()).thenReturn(Optional.empty());
    when(providerPort.getToken()).thenReturn(token);

    Optional<TokenModel> result = invokePrivateGetFreshToken();

    assertTrue(result.isPresent());
    assertEquals("provider-token", result.get().getAccessToken());
    verify(providerPort).getToken();
  }

  @Test
  void getFreshToken_whenGetTokenThrows_butRefreshTokenWorks_shouldReturnToken() {
    TokenModel token = new TokenModel(3600, "refreshed-token");

    when(cacheService.getToken()).thenThrow(new RuntimeException("cache fail"));
    when(providerPort.refreshToken()).thenReturn(token);

    Optional<TokenModel> result = invokePrivateGetFreshToken();

    assertTrue(result.isPresent());
    assertEquals("refreshed-token", result.get().getAccessToken());
    verify(providerPort).refreshToken();
  }

  @Test
  void getFreshToken_whenAllFails_shouldReturnEmpty() {
    when(cacheService.getToken()).thenThrow(new RuntimeException("fail"));
    when(providerPort.refreshToken()).thenThrow(new RuntimeException("also fail"));

    Optional<TokenModel> result = invokePrivateGetFreshToken();

    assertFalse(result.isPresent());
  }

  @Test
  void getValidAccessToken_whenTokenAvailable_shouldSaveToken() {
    TokenModel token = new TokenModel(3600, "token");
    when(cacheService.getToken()).thenReturn(Optional.of(token));

    tokenService.getValidAccessToken();

    verify(cacheService).saveToken(token);
  }

  @Test
  void getValidAccessToken_whenNoToken_shouldLogError() {
    when(cacheService.getToken()).thenReturn(Optional.empty());
    when(providerPort.getToken()).thenReturn(null);
    lenient().when(providerPort.refreshToken()).thenReturn(null);

    tokenService.getValidAccessToken();

    verify(cacheService, never()).saveToken(any());
  }

  private Optional<TokenModel> invokePrivateGetFreshToken() {
    try {
      var method = TokenService.class.getDeclaredMethod("getFreshToken");
      method.setAccessible(true);
      return (Optional<TokenModel>) method.invoke(tokenService);
    } catch (Exception e) {
      fail("Failed to invoke getFreshToken: " + e.getMessage());
      return Optional.empty();
    }
  }
}
