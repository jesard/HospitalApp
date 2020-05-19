package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Admin extends User {

    private int id;
    private String position;

    public Admin(int adminId, String firstName, String lastName, String patronymic, String login, String password, String position) {
        super(firstName, lastName, patronymic, login, password);
        id = adminId;
        this.position = position;
    }

    public Admin(String firstName, String lastName, String patronymic, String login, String password, String position) {
        this(0, firstName, lastName, patronymic, login, password, position);
    }



    public Admin() {

    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int userId) {
        this.id = userId;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin)) return false;
        Admin admin = (Admin) o;
        return Objects.equals(getPosition(), admin.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition());
    }


}
