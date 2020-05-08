package net.thumbtack.school.hospital.Service;

// REVU не надо использовать com.google.gson.Gson
// в Spring обычно используется jackson, но, как правило, вообще не надо вручную сериализовать и десериализовать
// см. REVU к методу login
import com.google.gson.Gson;
import net.thumbtack.school.hospital.dao.AdminDao;
import net.thumbtack.school.hospital.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.PatientDao;
import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.daoimpl.DoctorDaoImpl;
import net.thumbtack.school.hospital.daoimpl.PatientDaoImpl;
import net.thumbtack.school.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegUserDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegAdminDtoResponse;
import net.thumbtack.school.hospital.dto.response.RegPatientDtoResponse;
import net.thumbtack.school.hospital.dto.response.RegUserDtoResponse;
import net.thumbtack.school.hospital.dto.response.regDoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.User;

import java.util.UUID;

public class UserService {

    protected static final String ADMIN = "admin";
    protected static final String DOCTOR = "doctor";
    protected static final String PATIENT = "patient";

    protected Gson gson = new Gson();
    protected UserDao userDao = new UserDaoImpl();
    protected AdminDao adminDao = new AdminDaoImpl();
    protected DoctorDao doctorDao = new DoctorDaoImpl();
    protected PatientDao patientDao = new PatientDaoImpl();

    // REVU лучше User makeUserFromDtoRequest(RegUserDtoRequest request) {
    // и мне не нравится, что этот метод вызыается из других сервисов
    // лучше вынести его выше, то есть создать класс ServiceBase,
    // сделать все сервисы его наследниками
    // а этот метод перенести туда как protected
    public static void makeUserFromDtoRequest(User user, RegUserDtoRequest request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPatronymic(request.getPatronymic());
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
    }

    // REVU аналогично
    public static void makeDtoResponseFromUser(RegUserDtoResponse response, User user) {
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPatronymic(user.getPatronymic());
    }

    // REVU просто передайте методу LoginDtoRequest, а не String
    // контроллер сам его сделает
    public String login(String loginJson) {
        LoginDtoRequest request = gson.fromJson(loginJson, LoginDtoRequest.class);
        User user = userDao.getUser(request.getLogin());
        if (request.getPassword().equals(user.getPassword())) {
            String token = UUID.randomUUID().toString();
            userDao.login(user.getUserId(), token);
            return token;
        } else {
        	// REVU создайте свой класс ServerException с ErrorCode и выбрасывайте его
        	// не надо возвращать таинственные строки
            return "error";
        }
    }

    public String getUserByToken(String token) {
        int userId = userDao.getUserIdByToken(token);
        String descriptor = userDao.getDescriptorByUserId(userId);
        switch (descriptor) {
            case ADMIN: {
                Admin admin = adminDao.getAdminByUserId(userId);
                RegAdminDtoResponse response = AdminService.makeDtoResponseFromAdmin(admin);
                return gson.toJson(response);
            }
            case DOCTOR: {
                Doctor doctor = doctorDao.getDoctorByUserId(userId);
                RegDoctorDtoResponse response = DoctorService.makeDtoResponseFromDoctor(doctor);
                return gson.toJson(response);
            }
            case PATIENT: {
                Patient patient = patientDao.getPatientByUserId(userId);
                RegPatientDtoResponse response = PatientService.makeDtoResponseFromPatient(patient);
                return gson.toJson(response);
            }
            default:
            	// REVU А Вы подумали, где будет это исключение ловиться ?
            	// 
                throw new IllegalStateException("Unexpected value: " + descriptor);
        }
    }




}
