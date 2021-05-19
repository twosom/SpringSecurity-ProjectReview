package com.icloud.corespringsecurity.repository;

import com.icloud.corespringsecurity.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Account, Long> {

    @Query(
            "SELECT u " +
            "FROM Account u " +
            "JOIN FETCH u.roleList " +
            "WHERE u.username = :username"
    )
    Account findByUsername(@Param("username") String username);
}
