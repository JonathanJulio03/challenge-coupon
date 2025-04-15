package challenge.meli.coupon.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import challenge.meli.coupon.application.output.DbPort;
import challenge.meli.coupon.domain.RedemptionModel;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RedemptionServiceTest {

  @InjectMocks
  private RedemptionService redemptionService;

  @Mock
  private DbPort output;

  @Test
  void testRedemptionTop_shouldReturnSortedMapList() {
    RedemptionModel r1 = new RedemptionModel("item1", 5);
    RedemptionModel r2 = new RedemptionModel("item2", 10);
    RedemptionModel r3 = new RedemptionModel("item3", 7);

    when(output.getRedemptions()).thenReturn(List.of(r1, r2, r3));

    List<Map<String, Integer>> result = redemptionService.redemptionTop();

    assertEquals(3, result.size());
    assertEquals(Map.of("item2", 10), result.get(0));
    assertEquals(Map.of("item3", 7), result.get(1));
    assertEquals(Map.of("item1", 5), result.get(2));
  }
}
