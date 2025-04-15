package challenge.meli.coupon.infrastructure.output.adapter.db.repository;

import challenge.meli.coupon.domain.RedemptionModel;
import challenge.meli.coupon.infrastructure.output.adapter.db.entities.Redemption;
import java.util.List;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedemptionRepository extends MongoRepository<Redemption, String> {
  @Aggregation(pipeline = {
      "{ $group: { _id: \"$itemId\", count: { $sum: 1 } } }",
      "{ $sort: { count: -1 } }",
      "{ $limit: 5 }"
  })
  List<RedemptionModel> findTop5RedemptionsGroupedByItemId();
}
