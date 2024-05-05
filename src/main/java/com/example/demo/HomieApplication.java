package com.example.demo;

import com.example.demo.auth.RgstrReq;
import com.example.demo.auth.service.AuthSrv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.example.demo.sec.enums.RoleEnum.ADMIN;
import static com.example.demo.sec.enums.RoleEnum.SUPERADMIN;

@SpringBootApplication
public class HomieApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomieApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthSrv authSrv) {
		return args -> {
			RgstrReq superadmin = RgstrReq.builder()
					.usernm("superadmin")
					.eml("superadmin@gmail.com")
					.pw("superadmin")
					.role(SUPERADMIN)
					.build();
			System.out.println("Superadmin token: " + authSrv.register(superadmin).getAccessTkn());

			RgstrReq admin = RgstrReq.builder()
					.usernm("admin")
					.eml("admin@gmail.com")
					.pw("admin")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + authSrv.register(admin).getAccessTkn());
		};
	}

}
