package com.StudShare.service;

import com.StudShare.domain.RegToken;

public interface RegTokenManagerDao
{
    RegToken addRegToken(RegToken regToken);

    void deleteRegToken(RegToken regToken);

    RegToken findRegTokenByActivationKey(String token);

    RegToken findRegTokenById(RegToken regToken);
}
