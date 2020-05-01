package net.thumbtack.school.hospital;

import net.thumbtack.school.hospital.Service.AdminService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class HospitalServer {

    //private AdminService adminService = new AdminService();

    // REVU это здесь зачем ? метод должен вызываться из REST - контроллера - это не Задание 11
	public static String insertAdmin(String requestJsonString) {
        return AdminService.registerAdmin(requestJsonString);
    }

	//  REVU то же
    public static String login(String requestJsonString) {
        return null;
    }


    public static void main(final String[] args) {
        System.out.println("Start application");
        SpringApplication.run(HospitalServer.class);
    }
}
