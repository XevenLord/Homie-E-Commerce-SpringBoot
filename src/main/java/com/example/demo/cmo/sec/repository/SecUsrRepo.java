package com.example.demo.cmo.sec.repository;

import com.example.demo.cmo.sec.model.SecUsr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecUsrRepo extends JpaRepository<SecUsr, Long> {

    Optional<SecUsr> findByEml(String eml);

}
