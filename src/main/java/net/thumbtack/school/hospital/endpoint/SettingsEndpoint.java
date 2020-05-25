package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.response.GetSettingsDtoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SettingsEndpoint {

    @Value("${max_name_length}")
    private int maxNameLength;

    @Value("${min_password_length}")
    private int minPasswordLength;

    @GetMapping(value = "/settings", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetSettingsDtoResponse getSettings(@CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) {
        return new GetSettingsDtoResponse(maxNameLength, minPasswordLength);
    }
}
