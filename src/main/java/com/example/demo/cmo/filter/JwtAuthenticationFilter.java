package com.example.demo.cmo.filter;

import com.example.demo.cmo.base.JwtSrv;
import com.example.demo.sec.repository.SecTknRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtSrv jwtSrv;

    private final UserDetailsService usrDtlsSrv;

    private final SecTknRepo secTknRepo;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHdr = request.getHeader("Authorization");
        final String jwt;
        final String usrEml;
        if (authHdr == null || !authHdr.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHdr.substring(7);
        usrEml = jwtSrv.extractUsrnm(jwt);
        if (usrEml != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails usrDtls = this.usrDtlsSrv.loadUserByUsername(usrEml);
            boolean isTokenVld = secTknRepo.findByTkn(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (jwtSrv.isTokenValid(jwt, usrDtls) && isTokenVld) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        usrDtls,
                        null,
                        usrDtls.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
