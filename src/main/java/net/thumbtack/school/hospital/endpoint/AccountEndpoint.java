package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.Service.AdminService;
import net.thumbtack.school.hospital.Service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountEndpoint {

    UserService userService = new UserService();
    AdminService adminService = new AdminService();

    @GetMapping("/account")
    public String getUser(@CookieValue(value = "JAVASESSIONID", defaultValue = "") String token) {
        return userService.getUserByToken(token);
    }


}
