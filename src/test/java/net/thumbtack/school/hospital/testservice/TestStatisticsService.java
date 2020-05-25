package net.thumbtack.school.hospital.testservice;

import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.request.MakeAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegPatientDtoResponse;
import net.thumbtack.school.hospital.dto.response.StatsDoctorsDtoResponse;
import net.thumbtack.school.hospital.dto.response.regdoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.error.ServerException;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class TestStatisticsService extends TestServiceBase {

    @Test
    public void testGetDoctorsStats() throws ServerException {
        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);
        RegDoctorDtoResponse responseDoc1 = insertDoctor1(tokenAdmin);
        RegDoctorDtoResponse responseDoc2 = insertDoctor2(tokenAdmin);
        RegPatientDtoResponse response3 = insertPatientByService1();
        RegPatientDtoResponse response4 = insertPatientByService2();
        int docId1 = responseDoc1.getId();
        int docId2 = responseDoc2.getId();
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("faffasf", "fefefefefefef");
        String tokenPatient1 = userService.login(loginDtoRequest);
        LoginDtoRequest loginDtoRequest2= new LoginDtoRequest("kkkkiiky", "rfgergergwq");
        String tokenPatient2 = userService.login(loginDtoRequest2);
        MakeAppointmentDtoRequest makeAppointmentDtoRequest = new MakeAppointmentDtoRequest(docId1, "", "08-06-2020","09:00");
        patientService.makeAppointment(makeAppointmentDtoRequest, tokenPatient1);
        MakeAppointmentDtoRequest makeAppointmentDtoRequest2 = new MakeAppointmentDtoRequest(docId2, "", "15-06-2020","16:45");
        patientService.makeAppointment(makeAppointmentDtoRequest2, tokenPatient2);

        StatsDoctorsDtoResponse response = statisticsService.getDoctorsStatistics("", "08-06-2020", "20-06-2020", "", tokenAdmin);
        assertEquals(0, response.getDetailedByDoctor().size());
        assertEquals(2, response.getTotalDoctorsNumber());
        assertEquals(2, response.getTotalPatientsTaken());

        StatsDoctorsDtoResponse responseDetailed = statisticsService.getDoctorsStatistics("surgeon", "08-06-2020", "20-06-2020", "yes", tokenAdmin);
        assertEquals(1, responseDetailed.getTotalDoctorsNumber());
        assertEquals(1, responseDetailed.getTotalPatientsTaken());
        assertEquals(1, responseDetailed.getDetailedByDoctor().size());
        assertEquals(docId1, responseDetailed.getDetailedByDoctor().get(0).getId());
        assertEquals("surgeon", responseDetailed.getDetailedByDoctor().get(0).getSpeciality());

    }

}
