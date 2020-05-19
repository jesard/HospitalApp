package net.thumbtack.school.hospital.service;


import net.thumbtack.school.hospital.dao.*;
import net.thumbtack.school.hospital.daoimpl.*;
import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegUserDtoRequest;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.error.Field;
import net.thumbtack.school.hospital.error.MyError;
import net.thumbtack.school.hospital.dto.response.regdoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.error.ServerErrorCode;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    protected final String ADMIN = "admin";
    protected final String DOCTOR = "doctor";
    protected final String PATIENT = "patient";

    protected final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    protected final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

    protected UserDao userDao = new UserDaoImpl();
    protected AdminDao adminDao = new AdminDaoImpl();
    protected DoctorDao doctorDao = new DoctorDaoImpl();
    protected PatientDao patientDao = new PatientDaoImpl();

    protected void makeUserFromDtoRequest(User user, RegUserDtoRequest request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPatronymic(request.getPatronymic());
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
    }

    public String login(LoginDtoRequest request) throws ServerException {
        User user = userDao.getUser(request.getLogin());
        if (request.getPassword().equals(user.getPassword())) {
            String token = UUID.randomUUID().toString();
            userDao.login(user.getUserId(), token);
            return token;
        } else {
        	throw new ServerException(new MyError(ServerErrorCode.WRONG_LOGIN_OR_PASSWORD, Field.PASSWORD));
        }
    }

    public EmptyJsonResponse logout(String token) {
        userDao.logout(token);
        return new EmptyJsonResponse();
    }

    public RegUserDtoResponse getUserDtoResponseByToken(String token) throws ServerException {
        int userId = userDao.getUserIdByToken(token);
        String descriptor = userDao.getDescriptorByUserId(userId);
        switch (descriptor) {
            case ADMIN: {
                Admin admin = adminDao.getAdminByUserId(userId);
                return new RegAdminDtoResponse(admin.getId(), admin.getFirstName(), admin.getFirstName(), admin.getPatronymic(), admin.getPosition());
            }
            case DOCTOR: {
                Doctor doctor = doctorDao.getDoctorByUserId(userId);
                return new RegDoctorDtoResponse(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpeciality(), doctor.getRoom());
            }
            case PATIENT: {
                Patient patient = patientDao.getPatientByUserId(userId);
                return new RegPatientDtoResponse(patient.getId(),
                        patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                        patient.getEmail(), patient.getAddress(), patient.getPhone());
            }
            default:
                // REVU А Вы подумали, где будет это исключение ловиться ?
                //
                throw new IllegalStateException("Unexpected value: " + descriptor);
        }
    }

    public User getUserByToken(String token) throws ServerException {
        int userId = userDao.getUserIdByToken(token);
        String descriptor = userDao.getDescriptorByUserId(userId);
        switch (descriptor) {
            case ADMIN: {
                return adminDao.getAdminByUserId(userId);
            }
            case DOCTOR: {
                return doctorDao.getDoctorByUserId(userId);
            }
            case PATIENT: {
                return patientDao.getPatientByUserId(userId);
            }
            default:
                // REVU А Вы подумали, где будет это исключение ловиться ?
                //
                throw new IllegalStateException("Unexpected value: " + descriptor);
        }
    }

    public RegUserDtoResponse getUserDtoResponseByUserId(int userId) {
        String descriptor = userDao.getDescriptorByUserId(userId);
        switch (descriptor) {
            case ADMIN: {
                Admin admin = adminDao.getAdminByUserId(userId);
                return new RegAdminDtoResponse(admin.getId(), admin.getFirstName(), admin.getFirstName(), admin.getPatronymic(), admin.getPosition());
            }
            case DOCTOR: {
                Doctor doctor = doctorDao.getDoctorByUserId(userId);
                return new RegDoctorDtoResponse(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpeciality(), doctor.getRoom());
            }
            case PATIENT: {
                Patient patient = patientDao.getPatientByUserId(userId);
                return new RegPatientDtoResponse(patient.getId(),
                        patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                        patient.getEmail(), patient.getAddress(), patient.getPhone());
            }
            default:
                // REVU А Вы подумали, где будет это исключение ловиться ?
                //
                throw new IllegalStateException("Unexpected value: " + descriptor);
        }
    }

    public int getUserIdByToken(String token) throws ServerException {
        return userDao.getUserIdByToken(token);
    }

    public String getUserDecriptorByToken(String token) throws ServerException {
        int userId = userDao.getUserIdByToken(token);
        return userDao.getDescriptorByUserId(userId);
    }

    public EmptyJsonResponse deleteTicket(String ticketNumber, String token) throws ServerException {
        User user = getUserByToken(token);
        String descriptor = userDao.getDescriptorByUserId(user.getUserId());
        String emailAddress = "";
        String phoneNumber = "";
        if (descriptor.equals(DOCTOR)) {
            int doctorId = ((Doctor) user).getId();
            int doctorIdByTicket = doctorDao.getDoctorIdByTicketNumber(ticketNumber);
            if (doctorId != doctorIdByTicket) {
                throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
            }
        }
        if (descriptor.equals(PATIENT)) {
            Patient patient = (Patient) user;
            for (Slot slot: patient.getTickets()) {
                if (slot.getTicketNumber().equals(ticketNumber)) {
                    emailAddress = patient.getAddress();
                    phoneNumber = patient.getPhone();
                    break;
                }
            }
            if (emailAddress.length() == 0) {
                throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
            }
        }
        if (emailAddress.length() == 0) {
            int patientId = patientDao.getPatientIdByTicketNumber(ticketNumber);
            int userId = patientDao.getUserIdByPatientId(patientId);
            Patient patient = patientDao.getPatientByUserId(userId);
            emailAddress = patient.getAddress();
            phoneNumber = patient.getPhone();
        }
        patientDao.deleteTicket(ticketNumber);
        String subject = "Ticket" + ticketNumber + "canceled";
        sendEmail(emailAddress, subject, subject);
        sendSMS(phoneNumber, subject, subject);
        return new EmptyJsonResponse();
    }

    public void sendEmail(String address, String subject, String body) {
        LOGGER.debug("User service send email to {} with subject {}", address, subject);
    }

    public void sendSMS(String phoneNumber, String subject, String body) {
        LOGGER.debug("User service send SMS to number {} with subject {}", phoneNumber, subject);
    }


}
