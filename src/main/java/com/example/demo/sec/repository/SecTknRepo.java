package com.example.demo.sec.repository;

import com.example.demo.sec.enums.TknEnum;
import com.example.demo.sec.model.SecTkn;
import com.example.demo.sec.model.SecUsr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SecTknRepo extends JpaRepository<SecTkn, Long> {
    @Query(value = """
      select t from SecTkn t inner join SecUsr u\s
      on t.secUsr.id = u.id\s
      where u.id = :usrId and (t.expired = false or t.revoked = false)\s
      """)
    List<SecTkn> findAllValidTokenByUser(Long usrId);

    @Query(value = """
            select t from SecTkn t\s
            where t.secUsr.id = :usrId\s
            and t.tkn = :tkn\s
            and t.tknType = :tknType\s
            """)
    SecTkn findTknByUsr(@Param("usrId") Long usrId, @Param("tkn") String tkn, @Param("tknType") TknEnum tknType);

    Optional<SecTkn> findByTkn(String tkn);

    @Modifying
    @Query("delete from SecTkn t where t.secUsr = :secUsr and t.tknType = :tknType")
    int delByUsrAndTknType(@Param("secUsr") SecUsr secUsr,@Param("tknType") TknEnum tkntype);
}
