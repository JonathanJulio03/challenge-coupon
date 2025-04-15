package challenge.meli.coupon.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import challenge.meli.coupon.application.output.DbPort;
import challenge.meli.coupon.commons.exception.ErrorException;
import challenge.meli.coupon.domain.ItemModel;
import challenge.meli.coupon.domain.strategy.FillCouponGetItem;
import challenge.meli.coupon.domain.strategy.ItemFetcher;
import challenge.meli.coupon.infrastructure.input.adapter.rest.command.request.CouponRequest;
import challenge.meli.coupon.infrastructure.input.adapter.rest.command.response.CouponResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

  @InjectMocks
  private CouponService couponService;

  @Mock
  private ItemFetcher itemFetcher;

  @Mock
  private FillCouponGetItem fillCouponGetItem;

  @Mock
  private DbPort output;

  @Test
  void testCoupon_HappyPath() {
    List<String> itemIds = List.of("1", "2", "3", "3");
    List<ItemModel> fetchedItems = List.of(
        new ItemModel("1", 10.0),
        new ItemModel("2", 15.0),
        new ItemModel("3", 25.0)
    );
    List<ItemModel> selectedItems = List.of(
        new ItemModel("2", 15.0),
        new ItemModel("3", 25.0)
    );

    CouponRequest request = CouponRequest.builder()
        .itemIds(itemIds)
        .amount(50.0)
        .build();

    when(itemFetcher.fetch(any())).thenReturn(fetchedItems);
    when(fillCouponGetItem.get(fetchedItems, 50.0)).thenReturn(selectedItems);

    CouponResponse response = couponService.coupon(request);

    assertEquals(List.of("2", "3"), response.getItemIds());
    assertEquals(40.00, response.getTotal());

    verify(output).save(anyList());
  }

  @Test
  void testCoupon_SaveNotCalledWhenListIsEmpty() {
    List<String> itemIds = List.of("1", "2");
    List<ItemModel> fetchedItems = List.of(
        new ItemModel("1", 10.0),
        new ItemModel("2", 20.0)
    );
    List<ItemModel> selectedItems = List.of();

    CouponRequest request = CouponRequest.builder()
        .itemIds(itemIds)
        .amount(5.0)
        .build();

    when(itemFetcher.fetch(any())).thenReturn(fetchedItems);
    when(fillCouponGetItem.get(fetchedItems, 5.0)).thenReturn(selectedItems);

    CouponResponse response = couponService.coupon(request);

    assertTrue(response.getItemIds().isEmpty());
    assertEquals(0.00, response.getTotal());

    verify(output, never()).save(any());
  }

  @Test
  void testCoupon_InterruptedException() {
    CouponRequest request = CouponRequest.builder()
        .itemIds(List.of("1", "2"))
        .amount(30.0)
        .build();

    when(itemFetcher.fetch(any())).thenAnswer(invocation -> {
      throw new InterruptedException("Thread interrupted");
    });

    assertThrows(ErrorException.class, () -> couponService.coupon(request));
  }

  @Test
  void testCoupon_ExecutionException() {
    CouponRequest request = CouponRequest.builder()
        .itemIds(List.of("1", "2"))
        .amount(30.0)
        .build();

    when(itemFetcher.fetch(any())).thenAnswer(invocation -> {
      throw new ExecutionException(new RuntimeException("Error"));
    });

    assertThrows(ErrorException.class, () -> couponService.coupon(request));
  }

  @Test
  void testCoupon_IndexOutOfBoundsException() {
    CouponRequest request = CouponRequest.builder()
        .itemIds(List.of("1", "2"))
        .amount(30.0)
        .build();

    when(itemFetcher.fetch(any())).thenReturn(List.of(
        new ItemModel("1", 10.0)
    ));
    when(fillCouponGetItem.get(any(), anyDouble()))
        .thenThrow(new IndexOutOfBoundsException("Index error"));

    assertThrows(ErrorException.class, () -> couponService.coupon(request));
  }
}
