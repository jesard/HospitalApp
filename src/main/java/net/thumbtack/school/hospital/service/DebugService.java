package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.debug.DebugDaoImpl;
import net.thumbtack.school.hospital.dto.response.EmptyJsonResponse;

public class DebugService {

    DebugDaoImpl debugDao = new DebugDaoImpl();

    public EmptyJsonResponse clearDB() {
        debugDao.deleteAllUsers();
        return new EmptyJsonResponse();
    }
}
