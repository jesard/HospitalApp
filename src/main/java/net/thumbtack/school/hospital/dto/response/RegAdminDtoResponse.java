package net.thumbtack.school.hospital.dto.response;


public class RegAdminDtoResponse extends RegUserDtoResponse {

    private int id;
    private String position;

    public RegAdminDtoResponse(int id, String firstName, String lastName, String patronymic, String position) {
        super(firstName, lastName, patronymic);
        this.id = id;
        this.position = position;
    }


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
