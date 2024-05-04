package com.example.demo.cmo.config;

import com.example.demo.cmo.sec.repository.SecUsrRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class AplCfg {

    private final SecUsrRepo secUsrRepo;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> secUsrRepo.findByEml(username).orElseThrow(() -> new UsernameNotFoundException("User Not Exist."));
    }

}
