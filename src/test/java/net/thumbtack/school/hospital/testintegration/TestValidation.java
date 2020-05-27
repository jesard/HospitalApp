package net.thumbtack.school.hospital.testintegration;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thumbtack.school.hospital.dto.request.RegAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.regdoctor.RegDocDtoRequest;
import net.thumbtack.school.hospital.dto.request.regdoctor.WeekDaysSchedule;
import net.thumbtack.school.hospital.dto.request.regdoctor.WeekSchedule;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TestValidation extends TestRestTemplate {

    @Value("${max_name_length}")
    private int maxNameLength;

    @Value("${min_password_length}")
    private int minPasswordLength;


    private void regAdminInvalidParams(RegAdminDtoRequest admin) throws JsonProcessingException {
        String token = loginSuperAdmin();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "JAVASESSIONID=" + token);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> regAdminHttp = new HttpEntity<>(mapper.writeValueAsString(admin), requestHeaders);
        template.exchange(BASEURL + "/admins", HttpMethod.POST, regAdminHttp, String.class);
    }

    private void regDocInvalidParams(RegDocDtoRequest doctor) throws JsonProcessingException {
        String token = loginSuperAdmin();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "JAVASESSIONID=" + token);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> regDocRequest = new HttpEntity<>(mapper.writeValueAsString(doctor), requestHeaders);
        template.exchange(BASEURL + "/doctors", HttpMethod.POST, regDocRequest, String.class);
    }

    private void regPatientInvalidParams(RegPatientDtoRequest patient) throws JsonProcessingException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> regAdminHttp = new HttpEntity<>(mapper.writeValueAsString(patient), requestHeaders);
        template.exchange(BASEURL + "/patients", HttpMethod.POST, regAdminHttp, String.class);
    }


    @Test
    public void testInvalidJson() throws JsonProcessingException {
        String token = loginSuperAdmin();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "JAVASESSIONID=" + token);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> regAdminHttp = new HttpEntity<>("INVALID", requestHeaders);
        try {
            template.exchange(BASEURL + "/admins", HttpMethod.POST, regAdminHttp, String.class);
            fail();
        } catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertSame(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertTrue(responseString.contains("INVALID_JSON"));
        }
    }


    public static Stream<Arguments> loginParams() {
        return Stream.of(
                Arguments.arguments(""),
                Arguments.arguments((String) null),
                Arguments.arguments("wwefw#")
        );
    }

    @ParameterizedTest
    @MethodSource("loginParams")
    public void testInvalidLogin(String login) throws JsonProcessingException {
        RegAdminDtoRequest admin = new RegAdminDtoRequest("Иван", "Иванов", "Иванович", "", "qwerty", "junior admin");
        admin.setLogin(login);
        try {
            regAdminInvalidParams(admin);
            fail();
        } catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertTrue(responseString.contains("INVALID_LOGIN"));
        }

        admin.setLogin(RandomStringUtils.randomAlphabetic(maxNameLength + 1));
        try {
            regAdminInvalidParams(admin);
            fail();
        } catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertTrue(responseString.contains("login"));
            assertTrue(responseString.contains("INVALID_LOGIN"));
        }

    }

    public static Stream<Arguments> russianNameParams() {
        return Stream.of(
                Arguments.arguments("куаукаU"),
                Arguments.arguments((String) null),
                Arguments.arguments("@#$")
        );
    }

    @ParameterizedTest
    @MethodSource("russianNameParams")
    public void testInvalidRussianName(String name) throws JsonProcessingException {
        RegAdminDtoRequest admin = new RegAdminDtoRequest("Иван", "Иванов", "Иванович", "hhhhwwe", "qwerty", "junior admin");
        admin.setFirstName(name);
        try {
            regAdminInvalidParams(admin);
            fail();
        } catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertTrue(responseString.contains("firstName"));
            assertTrue(responseString.contains("INVALID_RUSSIAN_NAME"));
        }

    }

    @Test
    public void testMaxNameLength() throws JsonProcessingException {
        RegAdminDtoRequest admin = new RegAdminDtoRequest("Иван", "Иванов", "Иванович", "hhhhwwe", "qwerty", "junior admin");
        admin.setPassword(RandomStringUtils.randomAlphabetic(maxNameLength + 1));
        try {
            regAdminInvalidParams(admin);
            fail();
        } catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertTrue(responseString.contains("password"));
            assertTrue(responseString.contains("MAX_NAME_LENGTH"));
        }
    }

    public static Stream<Arguments> passwordParams() {
        return Stream.of(
                Arguments.arguments(""),
                Arguments.arguments((String) null),
                Arguments.arguments(" "),
                Arguments.arguments("adsf")
        );
    }

    @ParameterizedTest
    @MethodSource("passwordParams")
    public void testPasswordMinLength(String password) throws JsonProcessingException {
        RegAdminDtoRequest admin = new RegAdminDtoRequest("Иван", "Иванов", "Иванович", "hhhhwwe", "qwerty", "junior admin");
        admin.setPassword(password);
        try {
            regAdminInvalidParams(admin);
            fail();
        } catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertTrue(responseString.contains("password"));
            assertTrue(responseString.contains("PASSWORD_MIN_LENGTH"));
        }
    }


    @Test
    public void testInvalidDate() throws JsonProcessingException {
        RegDocDtoRequest doctor = new RegDocDtoRequest("Марат", "Веретенников", "Васильевич", "eregergwww", "vdfvffvdfv", "surgeon", "302a", "INVALID", "12-07-2020", 20);
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        WeekSchedule weekSchedule = new WeekSchedule("14:00", "17:00", weekDays);
        doctor.setWeekSchedule(weekSchedule);
        try {
            regDocInvalidParams(doctor);
            fail();
        } catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertTrue(responseString.contains("dateStart"));
            assertTrue(responseString.contains("INVALID_DATE"));
        }
    }

    @Test
    public void testInvalidTime() throws JsonProcessingException {
        RegDocDtoRequest doctor = new RegDocDtoRequest("Марат", "Веретенников", "Васильевич", "eregergwww", "vdfvffvdfv", "surgeon", "302a", "05-05-2020","12-07-2020", 20);
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        WeekSchedule weekSchedule = new WeekSchedule("INVALID", "17:00", weekDays);
        doctor.setWeekSchedule(weekSchedule);
        try {
            regDocInvalidParams(doctor);
            fail();
        } catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertTrue(responseString.contains("timeStart"));
            assertTrue(responseString.contains("INVALID_TIME"));
        }
    }

    @Test
    public void testInvalidWeekDay() throws JsonProcessingException {
        RegDocDtoRequest doctor = new RegDocDtoRequest("Марат", "Веретенников", "Васильевич", "eregergwww", "vdfvffvdfv", "surgeon", "302a", "05-05-2020", "12-07-2020", 20);
        List<String> weekDays = new ArrayList<>();
        weekDays.add("INVALID");
        WeekSchedule weekSchedule = new WeekSchedule("16:00", "17:00", weekDays);
        doctor.setWeekSchedule(weekSchedule);
        try {
            regDocInvalidParams(doctor);
            fail();
        } catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertTrue(responseString.contains("INVALID_DAY_OF_WEEK"));
        }
    }

    @Test
    public void testRegDoctorWithTwoSchedules() throws JsonProcessingException {
        RegDocDtoRequest doctor = new RegDocDtoRequest("Марат", "Веретенников", "Васильевич", "eregergwww", "vdfvffvdfv", "surgeon", "302a", "05-07-2020", "12-07-2020", 20);
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        WeekSchedule weekSchedule = new WeekSchedule("14:00", "17:00", weekDays);
        doctor.setWeekSchedule(weekSchedule);
        List<WeekDaysSchedule> weekDaysSchedules = new ArrayList<>();
        WeekDaysSchedule weekDaysSchedule = new WeekDaysSchedule("Thu", "12:00", "13:00");
        weekDaysSchedules.add(weekDaysSchedule);
        doctor.setWeekDaysSchedules(weekDaysSchedules);
        try {
            regDocInvalidParams(doctor);
            fail();
        } catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertTrue(responseString.contains("One schedule should be empty"));
        }
    }

    public static Stream<Arguments> phoneParams() {
        return Stream.of(
                Arguments.arguments((String) null),
                Arguments.arguments(""),
                Arguments.arguments(" "),
                Arguments.arguments("ferf"),
                Arguments.arguments("#"),
                Arguments.arguments("8"),
                Arguments.arguments("12345678901"),
                Arguments.arguments("8913441223"),
                Arguments.arguments("+78002223344"),
                Arguments.arguments("+7913222334")
        );
    }

    @ParameterizedTest
    @MethodSource("phoneParams")
    public void testInvalidPhoneNumber(String phone) throws JsonProcessingException {
        RegPatientDtoRequest patient = new RegPatientDtoRequest("Василий","Первый", "Иванович", "efdef@wefwe.ru", "frfrfersf", "+79845447788", "faffasf", "fvdfvde");
        patient.setPhone(phone);
        try {
            regPatientInvalidParams(patient);
            fail();
        } catch (HttpStatusCodeException ex) {
            String responseString = ex.getResponseBodyAsString();
            assertTrue(responseString.contains("phone"));
            assertTrue(responseString.contains("INVALID_MOBILE_PHONE"));
        }
    }



}
