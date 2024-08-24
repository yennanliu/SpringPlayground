package com.yen.webFluxPoc.repository;

import com.yen.webFluxPoc.model.Author;
import com.yen.webFluxPoc.model.UserInfo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface UserInfoRepository extends R2dbcRepository<UserInfo, Integer> {

    // https://youtu.be/h-njJqGL7g0?si=_rn-MgOa6H6HMJ_D&t=506
    @Query("SELECT u.* FROM user_info u INNER JOIN author a ON u.id = a.id WHERE u.id = ?")
    Flux<UserInfo> findUserByAuthorId(Integer id);



}
