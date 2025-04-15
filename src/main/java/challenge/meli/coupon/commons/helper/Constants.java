package challenge.meli.coupon.commons.helper;

public class Constants {

  private Constants() {
    super();
  }

  // Scheduler
  public static final String TOKEN_RATE = "${properties.schedulers.token.rate}";
  public static final String TOKEN_NAME = "${properties.schedulers.token.name}";

  // Util
  public static final String BRACKETS = "{}";
  public static final String COMMA = ",";
  public static final String REDIS_TOKEN_KEY = "provider:accessToken";
  public static final String REDIS_ITEM_KEY = "items:ids";
  public static final String SUCCESS = "200";
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String AUTHORIZATION_BEARER_TOKEN_FORMAT = "Bearer %s";

  // Default
  public static final String GRANT_TYPE_TOKEN = "authorization_code";
  public static final String GRANT_TYPE_REFRESH = "refresh_token";

  // Request Token
  public static final String GRANT_TYPE = "grant_type";
  public static final String CLIENT_ID = "client_id";
  public static final String CLIENT_SECRET = "client_secret";
  public static final String CODE = "code";
  public static final String REFRESH_TOKEN = "refresh_token";
  public static final String REDIRECT_URI = "redirect_uri";

  // Properties
  public static final String PROPERTIES_PREFIX = "properties";
  public static final String PROVIDER_PROPERTIES_PREFIX = "client.api";

  // Message
  public static final String ERROR_SAVE_REDIS = "Error save redis";
  public static final String ERROR_GET_TOKEN = "Error get token";
  public static final String ERROR_NOT_FOUND_TOKEN = "Error not found token";
  public static final String ERROR_THREAD_VIRTUAL = "Error thread virtual";

}
