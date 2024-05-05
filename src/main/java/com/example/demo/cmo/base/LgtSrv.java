package com.example.demo.cmo.base;

import com.example.demo.sec.model.SecTkn;
import com.example.demo.sec.repository.SecTknRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LgtSrv implements LogoutHandler {

    private final SecTknRepo secTknRepo;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHdr = request.getHeader("Authorization");
        final String jwt;
        if (authHdr == null || !authHdr.startsWith("Bearer ")) {
            return;
        }
        jwt = authHdr.substring(7);
        Optional<SecTkn> storedTknOptl = secTknRepo.findByTkn(jwt);
        if (storedTknOptl.isPresent()) {
            SecTkn storedTkn = storedTknOptl.get();
            storedTkn.setExpired(true);
            storedTkn.setRevoked(true);
            secTknRepo.save(storedTkn);
        }
    }
}
