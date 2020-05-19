package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.response.GetSettingsDtoResponse;
import org.springframework.beans.factory.annotation.Value;

public class SettingsService {

    @Value("${max_name_length}")
    private int maxNameLength;

    @Value("${min_password_length}")
    private int minPasswordLength;

    public GetSettingsDtoResponse getSettings(String token) {
        return new GetSettingsDtoResponse(maxNameLength, minPasswordLength);
    }
}
