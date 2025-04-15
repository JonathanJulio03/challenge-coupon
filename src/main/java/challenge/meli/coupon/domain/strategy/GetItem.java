package challenge.meli.coupon.domain.strategy;

import challenge.meli.coupon.domain.ItemModel;
import java.util.List;

public interface GetItem {
    List<ItemModel> get(List<ItemModel> items, double amount);
}
