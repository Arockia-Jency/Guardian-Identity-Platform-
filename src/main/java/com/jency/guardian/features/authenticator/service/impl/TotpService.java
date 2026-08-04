package com.jency.guardian.features.authenticator.service.impl;


public interface TotpService {

    boolean verify(String secretKey, String otp);

}