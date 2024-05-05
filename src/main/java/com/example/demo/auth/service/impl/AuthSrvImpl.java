package com.example.demo.auth.service.impl;

import com.example.demo.auth.AuthReq;
import com.example.demo.auth.AuthRsp;
import com.example.demo.auth.RgstrReq;
import com.example.demo.auth.service.AuthSrv;
import com.example.demo.cmo.base.JwtSrv;
import com.example.demo.sec.enums.RoleEnum;
import com.example.demo.sec.enums.TknEnum;
import com.example.demo.sec.model.SecTkn;
import com.example.demo.sec.model.SecUsr;
import com.example.demo.sec.repository.SecTknRepo;
import com.example.demo.sec.repository.SecUsrRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
                .role(RoleEnum.USER)
                .build();

        secUsrRepo.save(usr);
        String jwtToken = jwtSrv.generateToken(usr);
        saveUsrTkn(usr, jwtToken);
        return AuthRsp.builder()
                .token(jwtToken)
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
        revokeAllUsrTkns(usr);
        saveUsrTkn(usr, jwtToken);
        return AuthRsp.builder()
                .token(jwtToken)
                .build();
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
