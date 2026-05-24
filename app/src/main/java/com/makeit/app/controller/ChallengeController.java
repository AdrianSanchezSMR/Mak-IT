package com.makeit.app.controller;

import com.makeit.app.dto.challenge.ChallengeResponse;
import com.makeit.app.dto.challenge.CheckInResponse;
import com.makeit.app.service.ChallengeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @GetMapping("/today")
    public ChallengeResponse getTodayChallenge(@AuthenticationPrincipal String username) {
        return challengeService.getTodayChallenge(username);
    }

    @GetMapping("/today/all")
    public List<ChallengeResponse> getTodayChallenges(@AuthenticationPrincipal String username) {
        return challengeService.getTodayChallenges(username);
    }

    @GetMapping("/mine")
    public List<ChallengeResponse> getMyChallenges(@AuthenticationPrincipal String username) {
        return challengeService.getMyChallenges(username);
    }

    @PostMapping("/random")
    public ChallengeResponse assignRandomChallenge(@AuthenticationPrincipal String username) {
        return challengeService.assignRandomTodayChallenge(username);
    }

    @PutMapping("/{id}/checkin")
    public CheckInResponse checkIn(@AuthenticationPrincipal String username, @PathVariable("id") Long challengeId) {
        return challengeService.checkInTodayChallenge(username, challengeId);
    }
}
