package com.reservas.polo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

  // Ejemplo: dashboard del admin
  @GetMapping("/home")
  public String home() {
    return "home admin";
  }
}
