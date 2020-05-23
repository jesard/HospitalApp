package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.response.GetSettingsDtoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class SettingsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsService.class);

    @Value("${max_name_length}")
    private int maxNameLength;

    @Value("${min_password_length}")
    private int minPasswordLength;

    public GetSettingsDtoResponse getSettings(String token) {
        LOGGER.debug("Service get server settings");
        return new GetSettingsDtoResponse(maxNameLength, minPasswordLength);
    }
}
