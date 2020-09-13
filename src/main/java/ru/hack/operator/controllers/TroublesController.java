package ru.hack.operator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.hack.operator.dto.TroubleDto;
import ru.hack.operator.services.TroubleService;

import java.security.Principal;
import java.util.List;

@RestController
public class TroublesController {

    @Autowired
    private TroubleService troubleService;

    @GetMapping("/trouble")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TroubleDto>> getTroubles() {
        List<TroubleDto> troubles = troubleService.findNotDone();
        return ResponseEntity.ok(troubles);
    }

    // TODO : прикрутить свагер  документацию немного дописать

    @PostMapping("/trouble/{trouble-id}")
    @PreAuthorize("isAuthenticated()")
    public void changeStatus(@PathVariable("trouble-id") Long troubleId,
                             @RequestBody String newStatus) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        troubleService.changeStatus(troubleId, newStatus, authentication.getName());
    }
}
