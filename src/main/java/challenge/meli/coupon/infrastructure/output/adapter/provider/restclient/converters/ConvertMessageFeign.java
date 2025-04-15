package challenge.meli.coupon.infrastructure.output.adapter.provider.restclient.converters;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;

@Component
public class ConvertMessageFeign {

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(
            ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().toList());
    }
}