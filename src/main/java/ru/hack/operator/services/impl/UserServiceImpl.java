package ru.hack.operator.services.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hack.operator.dto.SignInDto;
import ru.hack.operator.dto.SignUpDto;
import ru.hack.operator.dto.TokenDto;
import ru.hack.operator.models.User;
import ru.hack.operator.repositories.UserRepository;
import ru.hack.operator.services.UserService;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    @Transactional
    public Optional<TokenDto> signIn(SignInDto signInDto) {
        Optional<User> userOptional = userRepository.findOneByUsername(signInDto.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(signInDto.getPassword(), user.getHashPassword())) {
                String token = Jwts.builder()
                        .setSubject(user.getId().toString())
                        .claim("name", user.getUsername())
                        .signWith(SignatureAlgorithm.HS256, secret)
                        .compact();
                return Optional.of(TokenDto.builder()
                        .token(token)
                        .userId(user.getId())
                        .username(user.getUsername())
                        .build());
            } else throw new IllegalArgumentException("Wrong email/password");
        } else throw new IllegalArgumentException("User not found");
    }

    @Override
    @Transactional
    public void signUp(SignUpDto signUpDto) {
        User user = User.builder()
                .email(signUpDto.getEmail())
                .username(signUpDto.getUsername())
                .hashPassword(passwordEncoder.encode(signUpDto.getPassword()))
                .build();

        userRepository.save(user);
    }
}
