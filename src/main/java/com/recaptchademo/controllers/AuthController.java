package com.recaptchademo.controllers;

import com.recaptchademo.dtos.ReCaptchaResponseDto;
import com.recaptchademo.dtos.RegisterDto;
import com.recaptchademo.services.ReCaptchaApiClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class AuthController {
    @Autowired
    private ReCaptchaApiClient reCaptchaApiClient;

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(RegisterDto registerDto) {
        log.info("{}", registerDto);

        ReCaptchaResponseDto reCaptchaResponseDto = reCaptchaApiClient.verify(registerDto.getRecaptchaResponse());
        log.info("{}", reCaptchaResponseDto);

        if (!reCaptchaResponseDto.isSuccess()) {
            throw new RuntimeException();
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
