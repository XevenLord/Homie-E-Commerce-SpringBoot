package com.example.demo.auth.service.impl;

import com.example.demo.auth.AuthReq;
import com.example.demo.auth.AuthRsp;
import com.example.demo.auth.RgstrReq;
import com.example.demo.auth.service.AuthSrv;
import com.example.demo.cmo.base.JwtSrv;
import com.example.demo.sec.enums.RoleEnum;
import com.example.demo.sec.model.SecUsr;
import com.example.demo.sec.repository.SecUsrRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthSrvImpl implements AuthSrv {

    private final SecUsrRepo secUsrRepo;

    private final PasswordEncoder pwEncoder;

    private final JwtSrv jwtSrv;

    private final AuthenticationManager authManager;

    @Override
    public AuthRsp register(RgstrReq rgstrReq) {
        SecUsr usr = SecUsr.builder()
                .usernm(rgstrReq.getUsernm())
                .eml(rgstrReq.getEml())
                .pw(pwEncoder.encode(rgstrReq.getPw()))
                .role(RoleEnum.CUSTOMER)
                .build();

        secUsrRepo.save(usr);
        return getAuthRspWithJwtToken(usr);
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
        return getAuthRspWithJwtToken(usr);
    }

    private AuthRsp getAuthRspWithJwtToken(SecUsr usr) {
        String jwtToken = jwtSrv.generateToken(usr);
        return AuthRsp.builder()
                .token(jwtToken)
                .build();
    }

}
