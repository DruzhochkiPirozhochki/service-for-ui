package ru.hack.operator.utils;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CoordinatesUtil {
    public Double[] coordinates(String coordinates) {
        return Arrays.stream(coordinates.replace("[", "")
                .replace("]", "")
                .split(","))
                .map(Double::parseDouble)
                .toArray(Double[]::new);
    }
}
