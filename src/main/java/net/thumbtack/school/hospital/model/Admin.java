package net.thumbtack.school.hospital.model;

public class Admin extends User {

    private String position;

    public Admin(String firstName, String lastName, String login, String password, String position) {
        super(firstName, lastName, login, password);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
