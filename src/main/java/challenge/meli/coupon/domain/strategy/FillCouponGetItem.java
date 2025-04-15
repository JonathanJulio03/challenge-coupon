package challenge.meli.coupon.domain.strategy;

import challenge.meli.coupon.domain.ItemModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FillCouponGetItem implements GetItem {

  @Override
  public List<ItemModel> get(List<ItemModel> items, double amount) {
    int n = items.size();
    int budgetInCents = (int) Math.round(amount * 100);

    double[] dp = new double[budgetInCents + 1];
    int[] itemIndex = new int[budgetInCents + 1];
    Arrays.fill(itemIndex, -1);

    for (int i = 0; i < n; i++) {
      int priceInCents = (int) Math.round(items.get(i).getPrice() * 100);
      double price = items.get(i).getPrice();

      for (int j = budgetInCents; j >= priceInCents; j--) {
        if (dp[j - priceInCents] + price > dp[j]) {
          dp[j] = dp[j - priceInCents] + price;
          itemIndex[j] = i;
        }
      }
    }

    List<ItemModel> selectedItems = new ArrayList<>();
    int j = budgetInCents;

    while (j > 0 && itemIndex[j] != -1) {
      int i = itemIndex[j];
      ItemModel item = items.get(i);
      selectedItems.add(item);
      j -= (int) Math.round(item.getPrice() * 100);
    }

    return selectedItems;
  }
}



