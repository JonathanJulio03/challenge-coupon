package challenge.meli.coupon.application.output;

import challenge.meli.coupon.domain.ItemModel;
import challenge.meli.coupon.domain.RedemptionModel;
import java.util.List;

public interface DbPort {

    void save(List<ItemModel> itemModel);
    List<RedemptionModel> getRedemptions();
}
