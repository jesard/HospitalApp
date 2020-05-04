package net.thumbtack.school.hospital.endpoint;


import net.thumbtack.school.hospital.Service.AdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminEndpoint {

    AdminService adminService = new AdminService();

    @PostMapping("/admin")
    public String insertAdmin(@RequestBody String requestJsonString) {
        return adminService.registerAdmin(requestJsonString);
    }

}
