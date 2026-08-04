package com.jency.guardian.features.authenticator.dto.response;

import java.util.List;

public class RecoveryCodesResponse {

    private List<String> recoveryCodes;

    public RecoveryCodesResponse(List<String> recoveryCodes) {
        this.recoveryCodes = recoveryCodes;
    }

    public List<String> getRecoveryCodes() {
        return recoveryCodes;
    }
}