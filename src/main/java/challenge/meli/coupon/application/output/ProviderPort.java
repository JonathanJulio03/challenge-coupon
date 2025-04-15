package challenge.meli.coupon.application.output;

import challenge.meli.coupon.domain.ItemModel;
import challenge.meli.coupon.domain.TokenModel;
import java.util.List;

public interface ProviderPort {
    TokenModel getToken();
    TokenModel refreshToken();
    List<ItemModel> getItems(String token,String items);
}
