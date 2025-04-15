package challenge.meli.coupon.application.service;

import static challenge.meli.coupon.commons.helper.Constants.*;

import challenge.meli.coupon.application.input.TokenUseCase;
import challenge.meli.coupon.application.output.ProviderPort;
import challenge.meli.coupon.domain.TokenModel;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TokenService implements TokenUseCase {

  private final ProviderPort output;
  private final CacheService cacheService;

  private Optional<TokenModel> getFreshToken() {
    try {
      return cacheService.getToken()
          .or(() -> Optional.ofNullable(output.getToken()));
    } catch (Exception e) {
      try {
        return Optional.ofNullable(output.refreshToken());
      } catch (Exception ex) {
        return Optional.empty();
      }
    }
  }

  @Override
  @Scheduled(fixedRateString = TOKEN_RATE)
  @SchedulerLock(name = TOKEN_NAME)
  public void getValidAccessToken() {
    getFreshToken().ifPresentOrElse(
        cacheService::saveToken,
        () -> log.error(ERROR_GET_TOKEN)
    );
  }
}