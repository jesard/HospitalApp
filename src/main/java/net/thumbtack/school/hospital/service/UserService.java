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
        LOGGER.debug("Service do login for user {}", request.getLogin());
        User user = userDao.getUserByLogin(request.getLogin());
        if (request.getPassword().equals(user.getPassword())) {
            String token = UUID.randomUUID().toString();
            userDao.login(user.getUserId(), token);
            return token;
        } else {
            LOGGER.info("Can't login for user {} - {}", request.getLogin(), ServerErrorCode.WRONG_LOGIN_OR_PASSWORD.getErrorString());
        	throw new ServerException(new MyError(ServerErrorCode.WRONG_LOGIN_OR_PASSWORD, Field.PASSWORD, request.getLogin()));
        }
    }

    public EmptyJsonResponse logout(String token) throws ServerException {
        LOGGER.debug("Service do logout");
        userDao.logout(token);
        return new EmptyJsonResponse();
    }

    public RegUserDtoResponse getUserDtoResponseByToken(String token) throws ServerException {
        LOGGER.debug("Service get UserDtoResponse by token");
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
        }
        LOGGER.info("Can't get UserDtoResponse by token");
        return null;
    }

    public User getUserByToken(String token) throws ServerException {
        LOGGER.debug("Service get User by token");
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
        }
        LOGGER.info("Can't get user by token");
        return null;
    }

    public RegUserDtoResponse getUserDtoResponseByUserId(int userId) throws ServerException {
        LOGGER.debug("Service get UserDtoResponse by user id {}", userId);
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
        }
        LOGGER.info("Can't get UserDtoResponse by user id {}", userId);
        return null;
    }

    public int getUserIdByToken(String token) throws ServerException {
        LOGGER.debug("Service get User id by token");
        return userDao.getUserIdByToken(token);
    }

    public String getUserDecriptorByToken(String token) throws ServerException {
        LOGGER.debug("Service get decriptor by token");
        int userId = userDao.getUserIdByToken(token);
        return userDao.getDescriptorByUserId(userId);
    }

    public EmptyJsonResponse deleteTicket(String ticketNumber, String token) throws ServerException {
        LOGGER.debug("Service delete ticket {}", ticketNumber);
        User user = getUserByToken(token);
        String descriptor = userDao.getDescriptorByUserId(user.getUserId());
        if (descriptor.equals(DOCTOR)) {
            int doctorId = ((Doctor) user).getId();
            int doctorIdByTicket = doctorDao.getDoctorIdByTicketNumber(ticketNumber);
            if (doctorId != doctorIdByTicket) {
                LOGGER.info("Can't delete ticket {} - {}", ticketNumber, ServerErrorCode.WRONG_USER.getErrorString());
                throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
            }
        }
        String emailAddress = "";
        String phoneNumber = "";
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
                LOGGER.info("Can't delete ticket {} - {}", ticketNumber, ServerErrorCode.WRONG_USER.getErrorString());
                throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
            }
        }
        //descriptor.equals(ADMIN)
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
