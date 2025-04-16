package challenge.meli.coupon.domain.strategy;

import challenge.meli.coupon.domain.ItemModel;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FillCouponGetItemTest {

  @InjectMocks
  private FillCouponGetItem fillCouponGetItem;

  @Mock
  private ResolutionStrategy resolutionStrategy;

  private BigInteger cents(double amount) {
    return BigDecimal.valueOf(amount)
        .setScale(2, RoundingMode.HALF_UP)
        .multiply(new BigDecimal("100"))
        .toBigIntegerExact();
  }

  @Test
  void testSelectItemsWithinBudget() {
    List<ItemModel> items = List.of(
        ItemModel.builder().id("1").price(2.0).build(),
        ItemModel.builder().id("2").price(3.0).build(),
        ItemModel.builder().id("3").price(1.5).build()
    );

    when(resolutionStrategy.getResolution(cents(5.0))).thenReturn(1);

    List<ItemModel> selected = fillCouponGetItem.get(items, 5.0);

    double total = selected.stream().mapToDouble(ItemModel::getPrice).sum();
    assertTrue(total <= 5.0);
    assertFalse(selected.isEmpty());
  }

  @Test
  void testNoItemsFitInBudget() {
    List<ItemModel> items = List.of(
        ItemModel.builder().id("1").price(10.0).build(),
        ItemModel.builder().id("2").price(20.0).build()
    );

    when(resolutionStrategy.getResolution(cents(5.0))).thenReturn(1);
    List<ItemModel> selected = fillCouponGetItem.get(items, 5.0);

    assertTrue(selected.isEmpty());
  }

  @Test
  void testEmptyItemList() {
    List<ItemModel> items = List.of();

    when(resolutionStrategy.getResolution(cents(10.0))).thenReturn(1);
    List<ItemModel> selected = fillCouponGetItem.get(items, 10.0);

    assertTrue(selected.isEmpty());
  }

  @Test
  void testExactBudgetMatch() {
    List<ItemModel> items = List.of(
        ItemModel.builder().id("1").price(2.5).build(),
        ItemModel.builder().id("2").price(2.5).build(),
        ItemModel.builder().id("3").price(1.0).build()
    );

    when(resolutionStrategy.getResolution(cents(5.0))).thenReturn(1);
    List<ItemModel> selected = fillCouponGetItem.get(items, 5.0);

    double total = selected.stream().mapToDouble(ItemModel::getPrice).sum();
    assertEquals(5.0, total, 0.001);
  }

  @Test
  void testRoundingEdgeCase() {
    List<ItemModel> items = List.of(
        ItemModel.builder().id("1").price(0.333).build(),
        ItemModel.builder().id("2").price(0.667).build()
    );

    when(resolutionStrategy.getResolution(cents(1.0))).thenReturn(1);
    List<ItemModel> selected = fillCouponGetItem.get(items, 1.0);

    double total = selected.stream().mapToDouble(ItemModel::getPrice).sum();
    assertEquals(1.0, total, 0.01);
  }
}