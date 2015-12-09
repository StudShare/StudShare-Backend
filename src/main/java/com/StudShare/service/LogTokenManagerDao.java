package com.StudShare.service;

import com.StudShare.domain.LogToken;

public interface LogTokenManagerDao
{
    LogToken addLogToken(LogToken logToken);

    void deleteLogToken(LogToken logToken);

    LogToken findLogTokenBySSID(String ssid);

    LogToken findLogTokenById(LogToken logToken);

}
