package lealceldeiro.springbootapp;

import java.util.UUID;

public class TestUtil {
  private TestUtil() {
  }

  public static String randomString() {
    return UUID.randomUUID().toString();
  }
}
