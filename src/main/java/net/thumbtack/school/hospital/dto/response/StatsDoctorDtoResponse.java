package net.thumbtack.school.hospital.dto.response;


public class StatsDoctorDtoResponse extends RegUserDtoResponse {

    private int id;
    private String speciality;
    private String workTime;
    private int patientsTaken;

    public StatsDoctorDtoResponse(int id, String firstName, String lastName, String patronymic, String speciality, String workTime, int patientsTaken) {
        super(firstName, lastName, patronymic);
        this.id = id;
        this.speciality = speciality;
        this.workTime = workTime;
        this.patientsTaken = patientsTaken;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public StatsDoctorDtoResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public int getPatientsTaken() {
        return patientsTaken;
    }

    public void setPatientsTaken(int patientsTaken) {
        this.patientsTaken = patientsTaken;
    }
}
