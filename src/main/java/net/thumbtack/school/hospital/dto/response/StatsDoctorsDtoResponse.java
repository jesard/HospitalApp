package net.thumbtack.school.hospital.dto.response;

import java.util.ArrayList;
import java.util.List;

public class StatsDoctorsDtoResponse {

    private String totalWorkTime;
    private int totalPatientsTaken;
    private int totalDoctorsNumber;
    private List<StatsDoctorDtoResponse> detailedByDoctor = new ArrayList<>();


    public StatsDoctorsDtoResponse(String totalWorkTime, int totalPatientsTaken, int totalDoctorsNumber, List<StatsDoctorDtoResponse> detailedByDoctor) {
        this.totalWorkTime = totalWorkTime;
        this.totalPatientsTaken = totalPatientsTaken;
        this.totalDoctorsNumber = totalDoctorsNumber;
        this.detailedByDoctor = detailedByDoctor;
    }

    public StatsDoctorsDtoResponse() {
    }

    public String getTotalWorkTime() {
        return totalWorkTime;
    }

    public void setTotalWorkTime(String totalWorkTime) {
        this.totalWorkTime = totalWorkTime;
    }

    public int getTotalPatientsTaken() {
        return totalPatientsTaken;
    }

    public void setTotalPatientsTaken(int totalPatientsTaken) {
        this.totalPatientsTaken = totalPatientsTaken;
    }

    public int getTotalDoctorsNumber() {
        return totalDoctorsNumber;
    }

    public void setTotalDoctorsNumber(int totalDoctorsNumber) {
        this.totalDoctorsNumber = totalDoctorsNumber;
    }

    public List<StatsDoctorDtoResponse> getDetailedByDoctor() {
        return detailedByDoctor;
    }

    public void setDetailedByDoctor(List<StatsDoctorDtoResponse> detailedByDoctor) {
        this.detailedByDoctor = detailedByDoctor;
    }
}
