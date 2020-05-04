package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.model.Admin;

public class RegAdminDtoResponse extends RegUserDtoResponse {

    private int id;
    private String position;

    public RegAdminDtoResponse() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
