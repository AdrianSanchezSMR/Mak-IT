package com.makeit.app.controller;

import com.makeit.app.dto.progress.ProgressSummaryResponse;
import com.makeit.app.service.ProgresoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me/progress")
public class ProgresoController {

    private final ProgresoService progresoService;

    public ProgresoController(ProgresoService progresoService) {
        this.progresoService = progresoService;
    }

    @GetMapping("/summary")
    public ProgressSummaryResponse getProgressSummary(@AuthenticationPrincipal String username) {
        return progresoService.getResumen(username);
    }
}
