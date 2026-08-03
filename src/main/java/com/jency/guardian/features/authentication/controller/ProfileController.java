package com.jency.guardian.features.authentication.controller;


import com.jency.guardian.features.authentication.dto.response.ProfileResponse;
import com.jency.guardian.features.authentication.service.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ProfileResponse getProfile() {
        return profileService.getProfile();
    }
}
