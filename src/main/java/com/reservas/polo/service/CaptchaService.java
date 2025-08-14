package com.reservas.polo.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.code.kaptcha.impl.DefaultKaptcha;

@Service
public class CaptchaService {
private final DefaultKaptcha kaptcha;
private final Cache<String, String> cache;

public CaptchaService(DefaultKaptcha kaptcha,
                     com.github.benmanes.caffeine.cache.Cache<String, String> cache) {
 this.kaptcha = kaptcha;
 this.cache = cache;
}

public Map<String, String> generate() throws IOException {
 String text = kaptcha.createText();
 BufferedImage img = kaptcha.createImage(text);

 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ImageIO.write(img, "png", baos);
 String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());

 String id = UUID.randomUUID().toString();
 cache.put(id, text); 

 return Map.of(
   "captchaId", id,
   "imageBase64", "data:image/png;base64," + base64
 );
}

/** Devuelve true si es correcto y además invalida el captcha. */
public boolean verifyAndInvalidate(String captchaId, String userCode) {
 String expected = cache.getIfPresent(captchaId);
 cache.invalidate(captchaId);
 if (expected == null || userCode == null) return false;
 return expected.equalsIgnoreCase(userCode.trim());
}
}
