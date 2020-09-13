package ru.hack.operator.services;

import ru.hack.operator.models.Image;

public interface CheckService {
    void sendToCheck(Image image);
}
