package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Admin extends User {

    private int id;
    private String position;

    public Admin(String firstName, String lastName, String login, String password, String position) {
        super(firstName, lastName, login, password);
        this.position = position;
    }

    public Admin(User user, String position) {
        super(user.getFirstName(), user.getLastName(), user.getLogin(), user.getPassword());
        this.position = position;
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
