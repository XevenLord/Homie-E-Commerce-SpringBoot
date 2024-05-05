package com.example.demo.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRsp {

    @JsonProperty("access_token")
    private String accessTkn;

    @JsonProperty("refresh_token")
    private String refreshTkn;
}
