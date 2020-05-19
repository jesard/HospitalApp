package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.response.StatsDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.StatsDoctorsDtoResponse;
import net.thumbtack.school.hospital.dto.response.StatsPatientDtoResponse;
import net.thumbtack.school.hospital.error.Field;
import net.thumbtack.school.hospital.error.MyError;
import net.thumbtack.school.hospital.error.ServerErrorCode;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatisticsService extends UserService {

    private int getDoctorAppointmentsNumber(int doctorId, LocalDate startDate, LocalDate endDate) {
        return doctorDao.getDoctorAppointmentsNumber(doctorId, startDate, endDate);
    }

    private int getDoctorWorkingMinutesBySchedule(int doctorId, LocalDate startDate, LocalDate endDate) {
        return doctorDao.getWorkingMinutesBySchedule(doctorId, startDate, endDate);
    }

    private int getPatientAppointmentsNumber(int patientId, String startDate, String endDate) {
        return patientDao.getPatientAppointmentsNumber(patientId, startDate, endDate);
    }

    public StatsDoctorDtoResponse getDoctorStatistics(int doctorId, String startDate, String endDate, boolean detailed, String token) throws ServerException {
        if (!(getUserDecriptorByToken(token).equals(ADMIN) || getUserDecriptorByToken(token).equals(DOCTOR))) {
            throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
        }
        LocalDate start = LocalDate.parse(startDate, formatterDate);
        LocalDate end = LocalDate.parse(endDate, formatterDate);
        int workMinutes = getDoctorWorkingMinutesBySchedule(doctorId, start, end);
        String workTime = workMinutes / 60 + " h " + workMinutes % 60 + " m";
        int patientsTaken = getDoctorAppointmentsNumber(doctorId, start, end);
        int userId = doctorDao.getUserIdByDoctorId(doctorId);
        Doctor doctor = doctorDao.getDoctorByUserId(userId);
        return new StatsDoctorDtoResponse(doctorId, doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpeciality(), workTime, patientsTaken);
    }

    public StatsDoctorsDtoResponse getDoctorsStatistics(String speciality, String startDate, String endDate, boolean detailed, String token) throws ServerException {
        if (!(getUserDecriptorByToken(token).equals(ADMIN) || getUserDecriptorByToken(token).equals(DOCTOR))) {
            throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
        }
        List<Integer> doctorIds;
        if (speciality != null) {
            doctorIds = doctorDao.getAllDoctorIdsBySpeciality(speciality);
        } else {
            doctorIds = doctorDao.getAllDoctorIds();
        }
        LocalDate start = LocalDate.parse(startDate, formatterDate);
        LocalDate end = LocalDate.parse(endDate, formatterDate);
        List<StatsDoctorDtoResponse> detailedByDoctor = new ArrayList<>();
        int totalWorkMinutes = 0;
        int totalPatientsTaken = 0;
        for (int doctorId: doctorIds) {
            int workMinutes = getDoctorWorkingMinutesBySchedule(doctorId, start, end);
            int patientsTaken = getDoctorAppointmentsNumber(doctorId, start, end);
            totalWorkMinutes += workMinutes;
            totalPatientsTaken += patientsTaken;
            if (detailed) {
                int userId = doctorDao.getUserIdByDoctorId(doctorId);
                Doctor doctor = doctorDao.getDoctorByUserId(userId);
                String workTime = workMinutes / 60 + " h " + workMinutes % 60 + " m";
                detailedByDoctor.add(new StatsDoctorDtoResponse(doctorId, doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpeciality(), workTime, patientsTaken));
            }
        }
        int totalDoctorsNumber = doctorIds.size();
        String totalWorkTime = totalWorkMinutes / 60 + " h " + totalWorkMinutes % 60 + " m";
        return new StatsDoctorsDtoResponse(totalWorkTime, totalPatientsTaken, totalDoctorsNumber, detailedByDoctor);

    }

    public StatsPatientDtoResponse getPatientStatistics(int patientId, String startDate, String endDate, String token) throws ServerException {
        int userId = getUserIdByToken(token);
        Patient patient = patientDao.getPatientByUserId(userId);
        if (getUserDecriptorByToken(token).equals(PATIENT)) {
            if (patient.getId() != patientId) {
                throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
            }
        }
        int appointmentsNumber = getPatientAppointmentsNumber(patientId, startDate, endDate);
        return new StatsPatientDtoResponse(patientId, patient.getFirstName(), patient.getLastName(), patient.getPatronymic(), patient.getEmail(), patient.getAddress(), patient.getPhone(), appointmentsNumber);
    }
}
