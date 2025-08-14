package com.reservas.polo.config;

import java.time.Duration;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Configuration
public class KaptchaConfig {
  @Bean
  public DefaultKaptcha kaptchaProducer() {
    DefaultKaptcha k = new DefaultKaptcha();
    Properties p = new Properties();
    p.setProperty("kaptcha.border", "no");
    p.setProperty("kaptcha.textproducer.char.length", "5");
    p.setProperty("kaptcha.textproducer.font.color", "black");
    p.setProperty("kaptcha.image.width", "160");
    p.setProperty("kaptcha.image.height", "50");
    p.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
    Config c = new Config(p);
    k.setConfig(c);
    return k;
  }

  @Bean
  public com.github.benmanes.caffeine.cache.Cache<String, String> captchaCache() {
    return Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(3))
        .maximumSize(10_000)
        .build();
  }
}
