package lealceldeiro.springbootapp.config;

import lealceldeiro.springbootapp.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AppConfTest {
  @Test
  void urlFor() {
    var baseUrl = TestUtil.randomString();
    var similarProductsUrl = TestUtil.randomString();
    var productDetailsUrl = TestUtil.randomString();

    var endpoints = Map.of(AppConf.Endpoint.PRODUCT_BASE_URL, baseUrl,
                           AppConf.Endpoint.SIMILAR_PRODUCTS, similarProductsUrl,
                           AppConf.Endpoint.PRODUCT_DETAILS, productDetailsUrl);

    AppConf appConf = new AppConf();
    appConf.setProductEndpoints(endpoints);

    assertEquals(baseUrl, appConf.urlFor(AppConf.Endpoint.PRODUCT_BASE_URL));
    assertEquals(similarProductsUrl, appConf.urlFor(AppConf.Endpoint.SIMILAR_PRODUCTS));
    assertEquals(productDetailsUrl, appConf.urlFor(AppConf.Endpoint.PRODUCT_DETAILS));
  }
}
