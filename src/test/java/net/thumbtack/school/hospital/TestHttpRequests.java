package net.thumbtack.school.hospital;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.thumbtack.school.hospital.debug.DebugDaoImpl;
import net.thumbtack.school.hospital.debug.HttpGetTest;
import net.thumbtack.school.hospital.endpoint.AdminEndpoint;
import net.thumbtack.school.hospital.model.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {AdminEndpoint.class, HttpGetTest.class})
public class TestHttpRequests {

    protected DebugDaoImpl debugDao = new DebugDaoImpl();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Gson gson;

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
        MvcResult result = mvc.perform(post("/api/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(admin)))
                .andReturn();
        assertEquals(result.getResponse().getStatus(), 200);
        JsonObject object = new JsonParser().parse(result.getResponse().getContentAsString()).getAsJsonObject();
        assertEquals(object.get("lastName").getAsString(), admin.getLastName());
    }

}
