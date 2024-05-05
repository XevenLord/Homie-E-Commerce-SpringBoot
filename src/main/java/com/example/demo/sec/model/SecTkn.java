package com.example.demo.sec.model;

import com.example.demo.sec.enums.TknEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SecTkn {
    @Id
    @GeneratedValue
    private Long id;

    private String tkn;

    @Enumerated(EnumType.STRING)
    private TknEnum tknType;

    private boolean expired;

    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "usr_id")
    private SecUsr secUsr;
}
