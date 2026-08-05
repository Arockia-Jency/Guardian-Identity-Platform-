package com.jency.guardian.features.profile.service.impl;

import com.jency.guardian.features.profile.dto.request.ChangePasswordRequest;
import com.jency.guardian.features.profile.dto.request.DeleteAccountRequest;
import com.jency.guardian.features.profile.dto.request.UpdateProfileRequest;
import com.jency.guardian.features.profile.dto.response.ChangePasswordResponse;
import com.jency.guardian.features.profile.dto.response.DeleteAccountResponse;
import com.jency.guardian.features.profile.dto.response.UpdateProfileResponse;
import com.jency.guardian.features.profile.dto.response.ProfileResponse;

public interface ProfileService {

    ProfileResponse getProfile();
    UpdateProfileResponse updateProfile(UpdateProfileRequest request);
    ChangePasswordResponse changePassword(ChangePasswordRequest request);
    DeleteAccountResponse deleteAccount(DeleteAccountRequest request);

}
