package ru.hack.operator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hack.operator.dto.TroubleDto;
import ru.hack.operator.services.TroubleService;

import java.util.List;

@Controller
public class TroublesController {

    @Autowired
    private TroubleService troubleService;

    @GetMapping("/trouble")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TroubleDto>> getTroubles() {
        List<TroubleDto> troubles = troubleService.findNotDone();
        return ResponseEntity.ok(troubles);
    }

    // TODO : проверить ето
    @PostMapping("/trouble/{trouble-id}")
    @PreAuthorize("isAuthenticated()")
    public void changeStatus(@PathVariable Long troubleId, @RequestParam String newStatus ) {
        troubleService.changeStatus(troubleId, newStatus);
    }
}
