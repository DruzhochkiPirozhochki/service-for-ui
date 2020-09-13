package ru.hack.operator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.hack.operator.dto.SignInDto;
import ru.hack.operator.dto.SignUpDto;
import ru.hack.operator.dto.TokenDto;
import ru.hack.operator.services.UserService;

import java.util.Optional;

@RestController
public class SignInController {

    @Autowired
    private UserService userService;

    @PostMapping("/signIn")
    public ResponseEntity<TokenDto> signIn(@RequestBody SignInDto signInData) {
        Optional<TokenDto> tokenDto = userService.signIn(signInData);
        return tokenDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/signUp")
    public ResponseEntity<TokenDto> signUp(@RequestBody SignUpDto signUpDto) {
        userService.signUp(signUpDto);
        return ResponseEntity.ok().build();
    }

}
