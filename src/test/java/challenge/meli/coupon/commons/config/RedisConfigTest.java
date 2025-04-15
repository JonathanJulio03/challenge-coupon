package challenge.meli.coupon.commons.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import challenge.meli.coupon.domain.ItemModel;
import challenge.meli.coupon.domain.TokenModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
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
  void redisConnectionFactoryShouldCreateJedisConnectionFactory() {
    String host = "localhost";
    int port = 6379;
    boolean useSsl = true;

    RedisConnectionFactory factory = redisConfig.redisConnectionFactory(host, port, useSsl);

    assertNotNull(factory);
    assertInstanceOf(JedisConnectionFactory.class, factory);

    JedisConnectionFactory jedisFactory = (JedisConnectionFactory) factory;

    RedisStandaloneConfiguration config = jedisFactory.getStandaloneConfiguration();
    assertEquals(host, config.getHostName());
    assertEquals(port, config.getPort());

    JedisClientConfiguration clientConfig = jedisFactory.getClientConfiguration();
    assertTrue(clientConfig.isUseSsl());
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
