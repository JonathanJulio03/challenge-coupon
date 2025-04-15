package challenge.meli.coupon.commons.properties;

import challenge.meli.coupon.commons.helper.Constants;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = Constants.PROPERTIES_PREFIX)
public class GlobalProperties {

    Scheduler schedulers;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Scheduler implements Serializable {
        Parameters token;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Parameters implements Serializable {
        String name;
        String rate;
    }
}
