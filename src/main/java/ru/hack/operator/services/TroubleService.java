package ru.hack.operator.services;

import ru.hack.operator.dto.TroubleDto;

import java.util.List;

public interface TroubleService {
    List<TroubleDto> findNotDone();

    void changeStatus(Long troubleId, String newStatus, String username);
}
