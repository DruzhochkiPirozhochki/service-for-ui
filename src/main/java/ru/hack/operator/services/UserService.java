package ru.hack.operator.services;

import ru.hack.operator.dto.SignInDto;
import ru.hack.operator.dto.SignUpDto;
import ru.hack.operator.dto.TokenDto;

import java.util.Optional;

public interface UserService {
    Optional<TokenDto> signIn(SignInDto signInDto);

    void signUp(SignUpDto signUpDto);
}
