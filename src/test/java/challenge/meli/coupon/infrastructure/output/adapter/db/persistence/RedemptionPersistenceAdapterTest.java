package challenge.meli.coupon.infrastructure.output.adapter.db.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import challenge.meli.coupon.domain.ItemModel;
import challenge.meli.coupon.domain.RedemptionModel;
import challenge.meli.coupon.infrastructure.output.adapter.db.mapper.DbMapper;
import challenge.meli.coupon.infrastructure.output.adapter.db.repository.RedemptionRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RedemptionPersistenceAdapterTest {

  @InjectMocks
  private RedemptionPersistenceAdapter redemptionPersistenceAdapter;

  @Mock
  private RedemptionRepository repository;

  @Spy
  private DbMapper mapper = Mappers.getMapper(DbMapper.class);

  @Test
  void testSave() {
    List<ItemModel> itemModelList = List.of(
        new ItemModel("item1", 10.0),
        new ItemModel("item2", 20.0)
    );

    redemptionPersistenceAdapter.save(itemModelList);

    verify(mapper).toEntity(itemModelList);

    verify(repository).saveAll(anyList());
  }

  @Test
  void testGetRedemptions() {
    List<RedemptionModel> expectedRedemptions = List.of(
        new RedemptionModel("item1", 5),
        new RedemptionModel("item2", 3)
    );

    when(repository.findTop5RedemptionsGroupedByItemId()).thenReturn(expectedRedemptions);

    List<RedemptionModel> actualRedemptions = redemptionPersistenceAdapter.getRedemptions();

    verify(repository).findTop5RedemptionsGroupedByItemId();

    assertEquals(expectedRedemptions, actualRedemptions);
  }
}
