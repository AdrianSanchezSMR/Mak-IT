package com.makeit.app.controller;

import com.makeit.app.dto.challenge.InterestsResponse;
import com.makeit.app.dto.challenge.UpdateInterestsRequest;
import com.makeit.app.service.ChallengeService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me")
public class UserInterestController {

    private final ChallengeService challengeService;

    public UserInterestController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @GetMapping("/interests")
    public InterestsResponse getInterests(@AuthenticationPrincipal String username) {
        return challengeService.getInterests(username);
    }

    @PutMapping("/interests")
    public InterestsResponse updateInterests(
            @AuthenticationPrincipal String username,
            @Valid @RequestBody UpdateInterestsRequest request
    ) {
        return challengeService.updateInterests(username, request);
    }
}
