package com.recaptchademo.dtos;

import lombok.Data;

@Data
public class RegisterDto {
    private String email;
    private String password;
    private String recaptchaResponse;
}
