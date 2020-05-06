package net.thumbtack.school.hospital;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.thumbtack.school.hospital.debug.DebugDaoImpl;
import net.thumbtack.school.hospital.debug.HttpGetTest;
import net.thumbtack.school.hospital.endpoint.AdminsEndpoint;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {AdminsEndpoint.class, HttpGetTest.class})
public class TestHttpRequests {

    protected DebugDaoImpl debugDao = new DebugDaoImpl();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Gson gson;

    private final String REGDOCJSON1 = "{" +
            "\"firstName\": \"John\"," +
            " \"lastName\": \"Watson\"," +
            " \"patronymic\": \"\"," +
            "\"speciality\":\"therapist\"," +
            "\"room\":\"555a\"," +
            "\"login\":\"login443\"," +
            "\"password\":\"qwerty\"," +
            "\"dateStart\":\"05-05-2020\"," +
            "\"dateEnd\":\"05-07-2020\"," +
            "\"weekSchedule\":" +
            "{" +
            "\"timeStart\" :\"09:00\"," +
            "\"timeEnd\":\"15:00\"," +
            "\"weekDays\":[]" +
            "}," +
            "\"weekDaysSchedule\":[]," +
            "\"duration\": 15}";

    @BeforeEach()
    public void clearDatabase() {
        debugDao.deleteAllUsers();
    }

    @Test
    public void testGetAll() throws Exception {
        mvc.perform(get("/api/test")).andExpect(status().isOk());
    }

    @Test
    public void testAdminPost() throws Exception {
        Admin admin = new Admin("Ivan", "Ivanov", "ivan322", "qwerty", "junior admin");
        MvcResult result = mvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(admin)))
                .andReturn();
        assertEquals(result.getResponse().getStatus(), 200);
        JsonObject object = new JsonParser().parse(result.getResponse().getContentAsString()).getAsJsonObject();
        assertEquals(object.get("lastName").getAsString(), admin.getLastName());
    }


//    @Test
//    public void testLoginUsingRestTemplate() throws Exception {
//
//        final String url1 = "http://localhost:8080/api/doctors";
//        final String url2 = "http://localhost:8080/api/sessions";
//        final String url3 = "http://localhost:8080/api/account";
//
//        RestTemplate template = new RestTemplate();
//
//        String loginJson = "{\"login\": \"login443\"," +
//                "\"password\": \"qwerty\"}";
//
//        HttpEntity<String> insertRequest = new HttpEntity<>(REGDOCJSON1);
//        HttpEntity<String> insertResponse = template.exchange(url1, HttpMethod.POST, insertRequest, String.class);
//
//        HttpEntity<String> loginRequest = new HttpEntity<>(loginJson);
//        HttpEntity<String> loginResponse = template.exchange(url2, HttpMethod.POST, loginRequest, String.class);
//        HttpHeaders headers = loginResponse.getHeaders();
//        String set_cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
//        String token = set_cookie != null ? set_cookie.replace("JAVASESSIONID=", "") : null;
//
//        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.add("Cookie", "JAVASESSIONID=" + token);
//
//        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
//
//        ResponseEntity response2 = template.exchange(url3, HttpMethod.GET, requestEntity, User.class);
//        User user = (User) response2.getBody();
//
//        if (user != null) {
//            assertEquals("Watson", user.getLastName());
//        } else fail();
//    }
}
