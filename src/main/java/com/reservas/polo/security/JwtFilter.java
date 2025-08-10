package com.reservas.polo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List; 

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtService jwt;

	public JwtFilter(JwtService jwt) {
		this.jwt = jwt;
	}

	/*@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
		if (auth != null && auth.startsWith("Bearer ")) {
			try {
				var claims = jwt.parse(auth.substring(7));
				String email = claims.getSubject();
				String role = (String) claims.get("role");
				var authToken = new UsernamePasswordAuthenticationToken(email, null,
						List.of(new SimpleGrantedAuthority(role)));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			} catch (Exception ignored) {
			}
		}
		chain.doFilter(req, res);
	}*/
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
	        throws ServletException, IOException {

	    String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
	    if (auth != null && auth.startsWith("Bearer ")) {
	        try {
	            var claims = jwt.parse(auth.substring(7));
	            String email = claims.getSubject();
	            @SuppressWarnings("unchecked")
	            List<String> roles = (List<String>) claims.get("authorities"); // 🔁 CAMBIO AQUÍ
	            var authorities = roles.stream()
	            		.map(r-> r.startsWith("ROLE_") ? r : "ROLE_"+r)
	            		.map(SimpleGrantedAuthority::new).toList();
	            var authToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
	            SecurityContextHolder.getContext().setAuthentication(authToken);
	            System.out.println("Token recibido: " + auth.substring(7));
	            System.out.println("Claims: " + claims);
	            System.out.println("Authorities mapeadas: " + authorities);

	        } catch (Exception e) {
	            e.printStackTrace(); // para ver si hay errores en consola
	        }
	    }
	    chain.doFilter(req, res);
	}

}
