package challenge.meli.coupon.infrastructure.output.adapter.provider.restclient.converters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.converter.HttpMessageConverter;

@ExtendWith(MockitoExtension.class)
class ConvertMessageFeignTest {

  @InjectMocks
  private ConvertMessageFeign convertMessageFeign;

  @Test
  @DisplayName("Should return HttpMessageConverters with empty list when no converters are provided")
  void messageConvertersWithEmptyListWhenNoConvertersProvided() {
    ObjectProvider<HttpMessageConverter<?>> converters = mock(ObjectProvider.class);
    when(converters.orderedStream()).thenReturn(Stream.empty());

    HttpMessageConverters result = convertMessageFeign.messageConverters(converters);

    assertNotNull(result);
  }

  @Test
  @DisplayName("Should return HttpMessageConverters with ordered list of provided converters")
  void messageConvertersWithOrderedListOfProvidedConverters() {
    ObjectProvider<HttpMessageConverter<?>> converters = mock(ObjectProvider.class);
    Stream<HttpMessageConverter<?>> converterStream = Stream.of(mock(HttpMessageConverter.class),
        mock(HttpMessageConverter.class));
    when(converters.orderedStream()).thenReturn(converterStream);

    HttpMessageConverters result = convertMessageFeign.messageConverters(converters);

    assertNotNull(result);
    verify(converters, times(1)).orderedStream();
  }
}