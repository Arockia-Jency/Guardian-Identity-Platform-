package com.jency.guardian.features.profile.controller;


import com.jency.guardian.features.profile.dto.request.ChangePasswordRequest;
import com.jency.guardian.features.profile.dto.request.DeleteAccountRequest;
import com.jency.guardian.features.profile.dto.request.UpdateProfileRequest;
import com.jency.guardian.features.profile.dto.response.ChangePasswordResponse;
import com.jency.guardian.features.profile.dto.response.DeleteAccountResponse;
import com.jency.guardian.features.profile.dto.response.UpdateProfileResponse;
import com.jency.guardian.features.profile.dto.response.ProfileResponse;
import com.jency.guardian.features.profile.service.impl.ProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ProfileResponse getProfile() {
        return profileService.getProfile();
    }

    @PutMapping
    public UpdateProfileResponse updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return profileService.updateProfile(request);
    }

    @PutMapping("/change-password")
    public ChangePasswordResponse changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return profileService.changePassword(request);
    }

    @DeleteMapping
    public DeleteAccountResponse deleteAccount(@Valid @RequestBody DeleteAccountRequest request) {
        return profileService.deleteAccount(request);
    }

}
