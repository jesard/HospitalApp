package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.response.GetSettingsDtoResponse;
import net.thumbtack.school.hospital.service.SettingsService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SettingsEndpoint {

    private SettingsService settingsService = new SettingsService();

    //GET /api/settings
    @GetMapping(value = "/settings", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetSettingsDtoResponse getSettings(@CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) {
        return settingsService.getSettings(token);
    }
}
