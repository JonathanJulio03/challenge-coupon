package challenge.meli.coupon.commons.config;

import challenge.meli.coupon.domain.ItemModel;
import challenge.meli.coupon.domain.TokenModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean(name = "tokenRedisTemplate")
    public RedisTemplate<String, TokenModel> tokenRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, TokenModel> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean(name = "itemRedisTemplate")
    public RedisTemplate<String, ItemModel> itemRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ItemModel> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<ItemModel> serializer = new Jackson2JsonRedisSerializer<>(ItemModel.class);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }
}

