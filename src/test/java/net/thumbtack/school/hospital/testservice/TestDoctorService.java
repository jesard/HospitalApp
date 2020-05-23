package net.thumbtack.school.hospital.testservice;

import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.response.regdoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.error.ServerException;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDoctorService extends TestServiceBase {

    @Test
    public void testDoctorRegister() throws ServerException {

        LoginDtoRequest loginAdmin = new LoginDtoRequest("admin", "qwerty");
        String tokenAdmin = userService.login(loginAdmin);

        RegDoctorDtoResponse response = insertDoctor2(tokenAdmin);
        assertEquals(response.getFirstName(), "Марат");
        assertEquals(response.getLastName(), "Веретенников");
        LocalDate dateFromDb = LocalDate.parse(response.getSchedule().get(0).getDate(), formatterDate);
        assertEquals(DayOfWeek.MONDAY, dateFromDb.getDayOfWeek());
    }

    @Test
    public void testDoctorRegisterWithWrongToken() {
        String token = "wrongToken";
        assertThrows(ServerException.class, ()-> insertDoctor2(token));
    }


}
