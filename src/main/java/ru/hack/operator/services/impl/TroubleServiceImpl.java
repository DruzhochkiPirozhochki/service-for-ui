package ru.hack.operator.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hack.operator.dto.TroubleDto;
import ru.hack.operator.models.Trouble;
import ru.hack.operator.models.User;
import ru.hack.operator.repositories.TroubleRepository;
import ru.hack.operator.repositories.UserRepository;
import ru.hack.operator.services.TroubleService;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TroubleServiceImpl implements TroubleService {
    @Autowired
    private TroubleRepository troubleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<TroubleDto> findNotDone() {
        return TroubleDto.from(troubleRepository.findAllToDo());
    }

    @Override
    @Transactional
    public void changeStatus(Long troubleId, String newStatus, String username) {
        Optional<User> userOptional = userRepository.findOneByUsername(username);

        Optional<Trouble> troubleOptional = troubleRepository.findById(troubleId);
        if (troubleOptional.isPresent()) {

            if (Arrays.stream(Trouble.Status.values())
                    .anyMatch(status -> status.toString().equals(newStatus))) {
                troubleOptional.get().setStatus(Trouble.Status.valueOf(newStatus));
                userOptional.ifPresent(user -> troubleOptional.get().setUser(user));
            }
        }
    }
}
