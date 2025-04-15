package challenge.meli.coupon.commons.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import challenge.meli.coupon.domain.ItemModel;
import challenge.meli.coupon.domain.TokenModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

class RedisConfigTest {

  private RedisConfig redisConfig;
  private RedisConnectionFactory mockConnectionFactory;

  @BeforeEach
  void setUp() {
    redisConfig = new RedisConfig();
    mockConnectionFactory = mock(RedisConnectionFactory.class);
  }

  @Test
  void testTokenRedisTemplate() {
    RedisTemplate<String, TokenModel> template = redisConfig.tokenRedisTemplate(mockConnectionFactory);

    assertNotNull(template);
    assertEquals(mockConnectionFactory, template.getConnectionFactory());

    assertInstanceOf(StringRedisSerializer.class, template.getKeySerializer());
    assertInstanceOf(GenericJackson2JsonRedisSerializer.class, template.getValueSerializer());
  }

  @Test
  void itemRedisTemplate_shouldCreateProperTemplate() {
    RedisTemplate<String, ItemModel> template = redisConfig.itemRedisTemplate(mockConnectionFactory);

    assertNotNull(template);
    assertEquals(mockConnectionFactory, template.getConnectionFactory());

    assertInstanceOf(StringRedisSerializer.class, template.getKeySerializer());
    assertInstanceOf(Jackson2JsonRedisSerializer.class, template.getValueSerializer());
  }
}
