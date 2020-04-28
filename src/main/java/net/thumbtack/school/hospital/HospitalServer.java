package net.thumbtack.school.hospital;

import net.thumbtack.school.hospital.Service.AdminService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@SpringBootApplication
public class HospitalServer {

    //private AdminService adminService = new AdminService();

    @PostMapping("/admin")
    public String insertAdmin(@RequestBody String requestJsonString) {
        return AdminService.registerAdmin(requestJsonString);
    }

    public static void main(final String[] args) {
        System.out.println("Start application");
        SpringApplication.run(HospitalServer.class);
    }

}
