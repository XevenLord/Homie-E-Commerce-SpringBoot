package com.example.demo.auth.service.impl;

import com.example.demo.auth.AuthReq;
import com.example.demo.auth.AuthRsp;
import com.example.demo.auth.RgstrReq;
import com.example.demo.auth.service.AuthSrv;
import com.example.demo.cmo.base.JwtSrv;
import com.example.demo.sec.enums.TknEnum;
import com.example.demo.sec.model.SecTkn;
import com.example.demo.sec.model.SecUsr;
import com.example.demo.sec.repository.SecTknRepo;
import com.example.demo.sec.repository.SecUsrRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthSrvImpl implements AuthSrv {

    private final SecUsrRepo secUsrRepo;

    private final SecTknRepo secTknRepo;

    private final PasswordEncoder pwEncoder;

    private final JwtSrv jwtSrv;

    private final AuthenticationManager authManager;

    @Override
    public AuthRsp register(RgstrReq rgstrReq) {
        SecUsr usr = SecUsr.builder()
                .usernm(rgstrReq.getUsernm())
                .eml(rgstrReq.getEml())
                .pw(pwEncoder.encode(rgstrReq.getPw()))
                .role(rgstrReq.getRole())
                .build();

        SecUsr savedUsr = secUsrRepo.save(usr);
        String jwtToken = jwtSrv.generateToken(usr);
        String refreshTkn = jwtSrv.generateRefreshToken(usr);
        saveUsrTkn(savedUsr, jwtToken);
        return AuthRsp.builder()
                .accessTkn(jwtToken)
                .refreshTkn(refreshTkn)
                .build();
    }

    @Override
    public AuthRsp authenticate(AuthReq authReq) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    authReq.getEml(),
                    authReq.getPw()
            )
        );
        SecUsr usr = secUsrRepo.findByEml(authReq.getEml()).orElseThrow();
        String jwtToken = jwtSrv.generateToken(usr);
        String refreshToken = jwtSrv.generateRefreshToken(usr);
        revokeAllUsrTkns(usr);
        saveUsrTkn(usr, jwtToken);
        return AuthRsp.builder()
                .accessTkn(jwtToken)
                .refreshTkn(refreshToken)
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHdr = request.getHeader("Authorization");
        final String refreshToken;
        final String usrEml;
        if (authHdr == null || !authHdr.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHdr.substring(7);
        usrEml = jwtSrv.extractUsrnm(refreshToken);
        if (usrEml != null) {
            var usr = this.secUsrRepo.findByEml(usrEml).orElseThrow();
            if (jwtSrv.isTokenValid(refreshToken, usr)) {
                var accessTkn = jwtSrv.generateToken(usr);
                revokeAllUsrTkns(usr);
                saveUsrTkn(usr, accessTkn);
                var authResponse = AuthRsp.builder()
                        .accessTkn(accessTkn)
                        .refreshTkn(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void revokeAllUsrTkns(SecUsr usr) {
        List<SecTkn> vldUsrTkns = secTknRepo.findAllValidTokenByUser(usr.getId());

        if (vldUsrTkns.isEmpty()) {
            return;
        }
        vldUsrTkns.forEach( t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        secTknRepo.saveAll(vldUsrTkns);
    }

    private void saveUsrTkn(SecUsr usr, String jwtToken) {
        SecTkn tkn = SecTkn.builder()
                .secUsr(usr)
                .tkn(jwtToken)
                .tknType(TknEnum.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        secTknRepo.save(tkn);
    }

}
