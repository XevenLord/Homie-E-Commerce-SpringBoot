package com.example.demo.cmo.config;

import com.example.demo.cmo.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.example.demo.sec.enums.RoleEnum.ADMIN;
import static com.example.demo.sec.enums.RoleEnum.SUPERADMIN;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecCfg {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/v1/auth/**").permitAll();
                    auth.requestMatchers("/api/v1/auth/**");
                    auth.requestMatchers("/api/v1/management/**").hasAnyRole(SUPERADMIN.name(), ADMIN.name());
                    auth.requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(SUPERADMIN.name(), ADMIN.name());
                    auth.requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(SUPERADMIN.name(), ADMIN.name());
                    auth.requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(SUPERADMIN.name(), ADMIN.name());
                    auth.requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(SUPERADMIN.name(), ADMIN.name());

                    auth.requestMatchers("/api/v1/admin/**").hasRole(SUPERADMIN.name());
                    auth.requestMatchers(GET, "/api/v1/admin/**").hasAuthority(SUPERADMIN.name());
                    auth.requestMatchers(POST, "/api/v1/admin/**").hasAuthority(SUPERADMIN.name());
                    auth.requestMatchers(PUT, "/api/v1/admin/**").hasAuthority(SUPERADMIN.name());
                    auth.requestMatchers(DELETE, "/api/v1/admin/**").hasAuthority(SUPERADMIN.name());
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout((logout) -> logout.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }

}
