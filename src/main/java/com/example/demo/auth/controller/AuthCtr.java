package com.example.demo.auth.controller;

import com.example.demo.auth.AuthReq;
import com.example.demo.auth.AuthRsp;
import com.example.demo.auth.RgstrReq;
import com.example.demo.auth.service.AuthSrv;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthCtr {

    private final AuthSrv authSrv;

    @PostMapping("/register")
    public ResponseEntity<AuthRsp> register(
            @RequestBody RgstrReq rgstrReq
            ) {
        return ResponseEntity.ok(authSrv.register(rgstrReq));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthRsp> authenticate(
            @RequestBody AuthReq authReq
    ) {
        return ResponseEntity.ok(authSrv.authenticate(authReq));
    }

}
