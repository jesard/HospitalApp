package net.thumbtack.school.hospital.dto.request;


import net.thumbtack.school.hospital.validation.*;


public class RegUserDtoRequest {

    @RussianName
    @MaxNameLength
    private String firstName;

    @RussianName
    @MaxNameLength
    private String lastName;

    @Patronymic
    @MaxNameLength
    private String patronymic;

    @Login
    private String login;

    @MaxNameLength
    @PasswordMinLength
    private String password;

    public RegUserDtoRequest(String firstName, String lastName, String patronymic, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
    }

    public RegUserDtoRequest() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
