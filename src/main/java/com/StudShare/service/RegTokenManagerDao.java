package com.StudShare.service;

import com.StudShare.domain.RegToken;

public interface RegTokenManagerDao
{
    RegToken addRegToken(RegToken regToken);

    void deleteRegToken(RegToken regToken);

    RegToken findRegTokenByActivationKey(String activationKey);

    RegToken findRegTokenById(RegToken regToken);
}
