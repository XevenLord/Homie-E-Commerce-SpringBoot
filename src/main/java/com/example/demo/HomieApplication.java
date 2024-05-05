package com.example.demo;

import com.example.demo.auth.RgstrReq;
import com.example.demo.auth.service.AuthSrv;
import com.example.demo.sec.enums.RoleEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomieApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomieApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthSrv authSrv) {
		return args -> {
			var superadmin = RgstrReq.builder()
					.usernm("superadmin")
					.eml("superadmin@gmail.com")
					.pw("superadmin")
					.role(RoleEnum.SUPERADMIN)
					.build();
			System.out.println("Superadmin token: " + authSrv.register(superadmin).getAccessTkn());

			var admin = RgstrReq.builder()
					.usernm("admin")
					.eml("admin@gmail.com")
					.pw("admin")
					.role(RoleEnum.ADMIN)
					.build();
			System.out.println("Admin token: " + authSrv.register(admin).getAccessTkn());
		};
	}

}
