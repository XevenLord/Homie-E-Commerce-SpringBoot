package com.example.demo.auth.service;

import com.example.demo.auth.AuthReq;
import com.example.demo.auth.AuthRsp;
import com.example.demo.auth.RgstrReq;
import com.example.demo.sec.repository.SecUsrRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthSrv {

    AuthRsp register(RgstrReq rgstrReq);

    AuthRsp authenticate(AuthReq authReq);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
