package challenge.meli.coupon.domain.strategy;

import challenge.meli.coupon.domain.ItemModel;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FillCouponGetItem implements GetItem {

  private final ResolutionStrategy resolutionStrategy;

  @Override
  public List<ItemModel> get(List<ItemModel> items, double budget) {
    final BigDecimal budgetDecimal = toScaledBigDecimal(budget);
    final BigInteger budgetInCents = toCents(budgetDecimal);
    final int resolution = resolutionStrategy.getResolution(budgetInCents);
    final BigInteger resolutionBI = BigInteger.valueOf(resolution);
    final BigInteger scaledBudget = budgetInCents.divide(resolutionBI);

    Map<BigInteger, BigDecimal> dp = new HashMap<>();
    Map<BigInteger, Integer> itemIndexMap = new HashMap<>();
    dp.put(BigInteger.ZERO, BigDecimal.ZERO);

    for (int i = 0; i < items.size(); i++) {
      ItemModel item = items.get(i);
      BigDecimal itemPrice = toScaledBigDecimal(item.getPrice());
      BigInteger itemCents = toCents(itemPrice);
      BigInteger scaledPrice = itemCents.divide(resolutionBI);

      Map<BigInteger, BigDecimal> updatedDp = new HashMap<>(dp);

      for (Map.Entry<BigInteger, BigDecimal> entry : dp.entrySet()) {
        BigInteger currentKey = entry.getKey();
        BigInteger newKey = currentKey.add(scaledPrice);

        if (newKey.compareTo(scaledBudget) > 0) {
          continue;
        }

        BigDecimal newValue = entry.getValue().add(itemPrice);
        if (newValue.compareTo(updatedDp.getOrDefault(newKey, BigDecimal.ZERO)) > 0) {
          updatedDp.put(newKey, newValue);
          itemIndexMap.put(newKey, i);
        }
      }

      dp = updatedDp;
    }

    BigInteger bestKey = dp.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(BigInteger.ZERO);

    return reconstructSelectedItems(items, itemIndexMap, bestKey, resolutionBI);
  }

  private BigDecimal toScaledBigDecimal(double value) {
    return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
  }

  private BigInteger toCents(BigDecimal value) {
    return value.multiply(BigDecimal.valueOf(100)).toBigIntegerExact();
  }

  private List<ItemModel> reconstructSelectedItems(List<ItemModel> items,
      Map<BigInteger, Integer> itemIndexMap,
      BigInteger bestKey, BigInteger resolutionBI) {
    List<ItemModel> selectedItems = new ArrayList<>();
    Set<Integer> usedIndexes = new HashSet<>();
    BigInteger currentKey = bestKey;

    while (currentKey.compareTo(BigInteger.ZERO) > 0 && itemIndexMap.containsKey(currentKey)) {
      int index = itemIndexMap.get(currentKey);
      if (usedIndexes.contains(index)) {
        break;
      }

      ItemModel item = items.get(index);
      selectedItems.add(item);
      usedIndexes.add(index);

      BigDecimal itemPrice = toScaledBigDecimal(item.getPrice());
      BigInteger itemCents = toCents(itemPrice);
      BigInteger scaledPrice = itemCents.divide(resolutionBI);
      currentKey = currentKey.subtract(scaledPrice);
    }

    return selectedItems;
  }

}


