package com.reservas.polo.dto;

public record LoginRequest(String email, String password, String captchaId, String captchaCode) {
}