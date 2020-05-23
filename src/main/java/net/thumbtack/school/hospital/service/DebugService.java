package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.debug.DebugDaoImpl;
import net.thumbtack.school.hospital.dto.response.EmptyJsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebugService.class);

    DebugDaoImpl debugDao = new DebugDaoImpl();

    public EmptyJsonResponse clearDB() {
        LOGGER.debug("Service clear Db");

        debugDao.deleteAllUsers();
        debugDao.deleteAllSessions();

        return new EmptyJsonResponse();
    }
}
