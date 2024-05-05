package com.example.demo.auth.service;

import com.example.demo.auth.AuthReq;
import com.example.demo.auth.AuthRsp;
import com.example.demo.auth.RgstrReq;
import com.example.demo.sec.repository.SecUsrRepo;

public interface AuthSrv {

    AuthRsp register(RgstrReq rgstrReq);

    AuthRsp authenticate(AuthReq authReq);

}
