package ru.hack.operator.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hack.operator.dto.TroubleDto;
import ru.hack.operator.models.Trouble;
import ru.hack.operator.repositories.TroubleRepository;
import ru.hack.operator.services.TroubleService;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TroubleServiceImpl implements TroubleService {
    @Autowired
    private TroubleRepository troubleRepository;

    @Override
    public List<TroubleDto> findNotDone() {
        return TroubleDto.from(troubleRepository.findAllToDo());
    }

    @Override
    @Transactional
    public void changeStatus(Long troubleId, String newStatus) {
        Optional<Trouble> troubleOptional = troubleRepository.findById(troubleId);
        if (troubleOptional.isPresent()) {
            if (Arrays.stream(Trouble.Status.values())
                    .anyMatch(status -> status.toString().equals(newStatus)))
                troubleOptional.get().setStatus(Trouble.Status.valueOf(newStatus));
        }
    }
}
