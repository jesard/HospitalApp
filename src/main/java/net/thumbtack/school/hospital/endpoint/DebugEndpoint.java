package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.response.EmptyJsonResponse;
import net.thumbtack.school.hospital.service.DebugService;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequestMapping("/api/debug")
public class DebugEndpoint {

    DebugService debugService = new DebugService();

    @PostMapping(value = "/clear", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyJsonResponse clear() {
        return debugService.clearDB();
    }
}
