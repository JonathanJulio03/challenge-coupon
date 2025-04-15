package challenge.meli.coupon.application.service;

import challenge.meli.coupon.application.input.RedemptionUseCase;
import challenge.meli.coupon.application.output.DbPort;
import challenge.meli.coupon.domain.RedemptionModel;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RedemptionService implements RedemptionUseCase {

    private final DbPort output;

    @Override
    public List<Map<String, Integer>> redemptionTop() {
        return output.getRedemptions().stream()
            .sorted(Comparator.comparingInt(RedemptionModel::getCount).reversed())
            .map(redemption -> Map.of(redemption.getId(), redemption.getCount()))
            .toList();
    }
}