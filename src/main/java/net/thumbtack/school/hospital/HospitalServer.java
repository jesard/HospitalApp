package net.thumbtack.school.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class HospitalServer {

    public static void main(final String[] args) {
    	// REVU прикрутите логгер
        System.out.println("Start application");
        SpringApplication.run(HospitalServer.class);
    }
}
