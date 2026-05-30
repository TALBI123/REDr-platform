package com.example.demo.auth.dto;

import com.example.demo.auth.principal.CustomUserDetails;

public record LoginResult(String token, CustomUserDetails principal) {}
