package challenge.meli.coupon.commons.properties;

import challenge.meli.coupon.commons.helper.Constants;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = Constants.PROVIDER_PROPERTIES_PREFIX)
public class ProviderProperties {

    String host;
    Credentials credentials;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Credentials implements Serializable {
        String clientId;
        String secret;
        String code;
        String redirect;
    }
}
