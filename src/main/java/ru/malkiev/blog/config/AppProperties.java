package ru.malkiev.blog.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Getter
public class AppProperties {

  private final Auth auth = new Auth();

  @Data
  public static class Auth {

    private String tokenSecret;
    private long tokenExpirationMsec;
  }
}
