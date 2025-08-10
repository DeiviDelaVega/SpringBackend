package com.reservas.polo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.util.Arrays;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
	private final Key key = Keys.hmacShaKeyFor("esta-es-una-clave-super-secreta-de-32+bytes!!".getBytes());
	private static final long EXP_MS = 24 * 60 * 60 * 1000; // 24h

	public String generate(String email, String role) {
		return Jwts.builder().setSubject(email).claim("authorities", Arrays.asList("ROLE_admin")) 
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXP_MS))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody(); 
    }
}
