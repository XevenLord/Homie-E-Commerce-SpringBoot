package com.example.demo.auth;

import com.example.demo.sec.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RgstrReq {
    private String usernm;
    private String eml;
    private String pw;
    private RoleEnum role;
}
