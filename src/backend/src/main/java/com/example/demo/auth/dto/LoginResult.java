package com.example.demo.auth.dto;

import com.example.demo.auth.principal.CustomUserPrincipal;

public record LoginResult(String token, CustomUserPrincipal principal) {}
