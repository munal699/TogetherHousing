package org.example.togetherhousing.repository;

import org.example.togetherhousing.model.UserTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<UserTbl, Integer> {

    Optional<UserTbl> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserTbl> findByFullname(String fullname);

    boolean existsByEmailAndPassword(String email, String password);
}