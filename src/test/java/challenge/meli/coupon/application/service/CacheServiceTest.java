package challenge.meli.coupon.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import challenge.meli.coupon.domain.ItemModel;
import challenge.meli.coupon.domain.TokenModel;
import java.time.Duration;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class CacheServiceTest {

  @Mock
  private RedisTemplate<String, TokenModel> tokenRedisTemplate;

  @Mock
  private RedisTemplate<String, ItemModel> itemRedisTemplate;

  @Mock
  private ValueOperations<String, TokenModel> tokenValueOps;

  @Mock
  private ValueOperations<String, ItemModel> itemValueOps;

  private CacheService cacheService;

  private static final String REDIS_TOKEN_KEY = "provider:accessToken";

  @BeforeEach
  void setUp() {
    cacheService = new CacheService(tokenRedisTemplate, itemRedisTemplate);
    lenient().when(tokenRedisTemplate.opsForValue()).thenReturn(tokenValueOps);
    lenient().when(itemRedisTemplate.opsForValue()).thenReturn(itemValueOps);
  }

  @Test
  void getToken_shouldReturnToken() {
    TokenModel token = TokenModel.builder()
        .accessToken("abc123")
        .expiresIn(1000)
        .build();

    when(tokenValueOps.get(REDIS_TOKEN_KEY)).thenReturn(token);

    Optional<TokenModel> result = cacheService.getToken();

    assertTrue(result.isPresent());
    assertEquals("abc123", result.get().getAccessToken());
  }

  @Test
  void saveToken_shouldSaveWithoutException() {
    TokenModel token = TokenModel.builder()
        .accessToken("abc123")
        .expiresIn(1000)
        .build();

    doNothing().when(tokenValueOps).set(eq(REDIS_TOKEN_KEY), eq(token), any(Duration.class));

    cacheService.saveToken(token);

    verify(tokenValueOps).set(eq(REDIS_TOKEN_KEY), eq(token), any(Duration.class));
  }

  @Test
  void getItem_shouldReturnItem() {
    String id = "item1";
    ItemModel item = ItemModel.builder()
        .id(id)
        .price(12.5)
        .build();

    when(itemValueOps.get(id)).thenReturn(item);

    Optional<ItemModel> result = cacheService.getItem(id);

    assertTrue(result.isPresent());
    assertEquals(12.5, result.get().getPrice());
  }

  @Test
  void getItem_shouldReturnEmptyOnException() {
    String id = "item1";

    when(itemValueOps.get(id)).thenThrow(new RuntimeException("Redis error"));

    Optional<ItemModel> result = cacheService.getItem(id);

    assertFalse(result.isPresent());
  }

  @Test
  void saveItem_shouldSaveItem() {
    String id = "item1";
    ItemModel item = ItemModel.builder()
        .id(id)
        .price(15.0)
        .build();

    doNothing().when(itemValueOps).set(eq(id), eq(item), eq(Duration.ofMinutes(10)));

    cacheService.saveItem(id, item);

    verify(itemValueOps).set(eq(id), eq(item), eq(Duration.ofMinutes(10)));
  }
}
