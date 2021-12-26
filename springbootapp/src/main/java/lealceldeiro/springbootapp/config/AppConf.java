package lealceldeiro.springbootapp.config;

import java.util.Collections;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConf {
  public static final class Endpoint {
    public static final String PRODUCT_BASE_URL = "product-base-url";
    public static final String SIMILAR_PRODUCTS = "similar-products";
    public static final String PRODUCT_DETAILS = "product-details";

    private Endpoint() {
    }
  }

  @Getter(AccessLevel.NONE)
  private Map<String, String> productEndpoints = Collections.emptyMap();

  /**
   * Returns the configured endpoint value.
   *
   * @param endpointName Endpoint name.
   *
   * @return A String value, the endpoint, or null if none was configured with the specified
   * {@code endpointName}.
   *
   * @see Endpoint
   */
  public String urlFor(String endpointName) {
    return productEndpoints.get(endpointName);
  }
}
