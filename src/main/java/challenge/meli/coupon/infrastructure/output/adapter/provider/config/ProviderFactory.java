package challenge.meli.coupon.infrastructure.output.adapter.provider.config;

import static challenge.meli.coupon.commons.helper.Constants.*;

import challenge.meli.coupon.commons.properties.ProviderProperties;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class ProviderFactory {

    private final ProviderProperties properties;

    public MultiValueMap<String, String> postTokenRequest() {
        return buildRequestBody(Map.ofEntries(
            Map.entry(GRANT_TYPE, GRANT_TYPE_TOKEN),
            Map.entry(CLIENT_ID, properties.getCredentials().getClientId()),
            Map.entry(CLIENT_SECRET, properties.getCredentials().getSecret()),
            Map.entry(CODE, properties.getCredentials().getCode()),
            Map.entry(REDIRECT_URI, properties.getCredentials().getRedirect())
        ));
    }

    public MultiValueMap<String, String> postRefreshTokenRequest() {
        return buildRequestBody(Map.ofEntries(
            Map.entry(GRANT_TYPE, GRANT_TYPE_REFRESH),
            Map.entry(CLIENT_ID, properties.getCredentials().getClientId()),
            Map.entry(CLIENT_SECRET, properties.getCredentials().getSecret()),
            Map.entry(REFRESH_TOKEN, properties.getCredentials().getCode())
        ));
    }

    protected MultiValueMap<String, String> buildRequestBody(Map<String, String> entries) {
        var body = new LinkedMultiValueMap<String, String>();
        entries.forEach(body::add);
        return body;
    }
}
