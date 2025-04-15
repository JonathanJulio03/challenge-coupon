package challenge.meli.coupon.domain.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import challenge.meli.coupon.application.output.ProviderPort;
import challenge.meli.coupon.application.service.CacheService;
import challenge.meli.coupon.domain.ItemModel;
import challenge.meli.coupon.domain.TokenModel;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemFetcherTest {

  @InjectMocks
  private ItemFetcher itemFetcher;

  @Mock
  private CacheService cacheService;

  @Mock
  private ProviderPort output;

  private final TokenModel tokenModel = TokenModel.builder()
      .accessToken("mock-token")
      .expiresIn(3600000)
      .build();

  private final ItemModel item1 = ItemModel.builder().id("1").price(100.0).build();
  private final ItemModel item2 = ItemModel.builder().id("2").price(200.0).build();

  @Test
  void shouldReturnItemsFromCacheOnly() {
    when(cacheService.getItem("1")).thenReturn(Optional.of(item1));
    when(cacheService.getItem("2")).thenReturn(Optional.of(item2));

    List<ItemModel> result = itemFetcher.fetch(List.of("1", "2"));

    assertEquals(2, result.size());
    assertTrue(result.contains(item1));
    assertTrue(result.contains(item2));
    verifyNoInteractions(output);
  }

  @Test
  void shouldFetchItemsFromProviderWhenNotInCache() {
    when(cacheService.getItem("1")).thenReturn(Optional.empty());
    when(cacheService.getItem("2")).thenReturn(Optional.empty());
    when(cacheService.getToken()).thenReturn(Optional.of(tokenModel));

    when(output.getItems("Bearer mock-token", "1,2")).thenReturn(List.of(item1, item2));

    List<ItemModel> result = itemFetcher.fetch(List.of("1", "2"));

    assertEquals(2, result.size());
    verify(cacheService).saveItem("1", item1);
    verify(cacheService).saveItem("2", item2);
  }

  @Test
  void shouldHandleMixedCacheAndProviderItems() {
    when(cacheService.getItem("1")).thenReturn(Optional.of(item1));
    when(cacheService.getItem("2")).thenReturn(Optional.empty());
    when(cacheService.getToken()).thenReturn(Optional.of(tokenModel));

    when(output.getItems("Bearer mock-token", "2")).thenReturn(List.of(item2));

    List<ItemModel> result = itemFetcher.fetch(List.of("1", "2"));

    assertEquals(2, result.size());
    verify(cacheService).saveItem("2", item2);
  }

  @Test
  void shouldFilterOutNullItemsFromProvider() {
    when(cacheService.getItem("1")).thenReturn(Optional.empty());
    when(cacheService.getToken()).thenReturn(Optional.of(tokenModel));

    when(output.getItems("Bearer mock-token", "1")).thenReturn(List.of(item1));

    List<ItemModel> result = itemFetcher.fetch(List.of("1"));

    assertEquals(1, result.size());
    assertTrue(result.contains(item1));
    verify(cacheService).saveItem("1", item1);
  }

  @Test
  void shouldHandleLargeBatchingOver20Items() {
    List<String> ids = IntStream.rangeClosed(1, 45).mapToObj(String::valueOf).toList();

    ids.forEach(id -> when(cacheService.getItem(id)).thenReturn(Optional.empty()));
    when(cacheService.getToken()).thenReturn(Optional.of(tokenModel));

    when(output.getItems(anyString(), anyString()))
        .thenAnswer(invocation -> {
          String items = invocation.getArgument(1);
          return Arrays.stream(items.split(","))
              .map(id -> ItemModel.builder().id(id).price(10.0).build())
              .toList();
        });

    List<ItemModel> result = itemFetcher.fetch(ids);

    assertEquals(45, result.size());
    verify(output, times(3)).getItems(anyString(), anyString());
  }
}
