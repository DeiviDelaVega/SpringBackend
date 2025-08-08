package com.reservas.polo.service;

public interface AuthService {
	
	AuthUser authenticate(String username, String rawPassword);
	record AuthUser(String email, String role) {}
	
}
