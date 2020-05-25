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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatisticsService extends UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsService.class);

    private int getDoctorAppointmentsNumber(int doctorId, LocalDate startDate, LocalDate endDate) throws ServerException {
        return doctorDao.getDoctorAppointmentsNumber(doctorId, startDate, endDate);
    }

    private int getDoctorWorkingMinutesBySchedule(int doctorId, LocalDate startDate, LocalDate endDate) throws ServerException {
        return doctorDao.getWorkingMinutesBySchedule(doctorId, startDate, endDate);
    }

    private int getPatientAppointmentsNumber(int patientId, LocalDate startDate, LocalDate endDate) {
        return patientDao.getPatientAppointmentsNumber(patientId, startDate, endDate);
    }

    public StatsDoctorDtoResponse getDoctorStatistics(int doctorId, String startDate, String endDate, String token) throws ServerException {
        LOGGER.debug("Service get stats for doctor with id {}, dates {} - {}", doctorId, startDate, endDate);
        if (!(getUserDecriptorByToken(token).equals(ADMIN) || getUserDecriptorByToken(token).equals(DOCTOR))) {
            LOGGER.info("Can't  get stats for doctor with id {}, dates {} - {}: {}", doctorId, startDate, endDate, ServerErrorCode.WRONG_USER.getErrorString());
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

    public StatsDoctorsDtoResponse getDoctorsStatistics(String speciality, String startDate, String endDate, String detailed, String token) throws ServerException {
        LOGGER.debug("Service get stats for doctors, speciality = {}, dates {} - {}", speciality, startDate, endDate);
        if (!(getUserDecriptorByToken(token).equals(ADMIN) || getUserDecriptorByToken(token).equals(DOCTOR))) {
            LOGGER.info("Can't  get stats for doctors, speciality = {}, dates {} - {}: {}", speciality, startDate, endDate, ServerErrorCode.WRONG_USER.getErrorString());
            throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
        }
        List<Integer> doctorIds;
        if (speciality != null && speciality.length() > 0) {
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
            if (detailed.equals("yes")) {
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
        LocalDate start = LocalDate.parse(startDate, formatterDate);
        LocalDate end = LocalDate.parse(endDate, formatterDate);
        int appointmentsNumber = getPatientAppointmentsNumber(patientId, start, end);
        return new StatsPatientDtoResponse(patientId, patient.getFirstName(), patient.getLastName(), patient.getPatronymic(), patient.getEmail(), patient.getAddress(), patient.getPhone(), appointmentsNumber);
    }
}
