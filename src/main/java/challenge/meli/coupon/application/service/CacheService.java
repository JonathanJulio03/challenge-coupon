package challenge.meli.coupon.application.service;

import static challenge.meli.coupon.commons.helper.Constants.*;

import challenge.meli.coupon.domain.ItemModel;
import challenge.meli.coupon.domain.TokenModel;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {

  private final RedisTemplate<String, TokenModel> tokenRedisTemplate;

  private final RedisTemplate<String, ItemModel> itemRedisTemplate;

  public Optional<TokenModel> getToken() {
    return Optional.of(tokenRedisTemplate.opsForValue().get(REDIS_TOKEN_KEY));
  }

  public void saveToken(TokenModel token) {
    try {
      tokenRedisTemplate.opsForValue().set(
          REDIS_TOKEN_KEY,
          token,
          Duration.ofMillis(token.getExpiresIn())
      );
    } catch (Exception e) {
      log.error(e.getMessage());
      log.error(ERROR_SAVE_REDIS);
    }
  }

  public Optional<ItemModel> getItem(String id) {
    try {
      return Optional.of(itemRedisTemplate.opsForValue().get(id));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public void saveItem(String id, ItemModel item) {
    itemRedisTemplate.opsForValue().set(id, item, Duration.ofMinutes(10));
  }
}
