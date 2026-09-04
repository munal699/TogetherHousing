package org.example.togetherhousing.repository;

import org.example.togetherhousing.model.UserTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




// repository communicate with the model tools for crud operations
// Rules:
// 1. model table needs to be provided to repository
// 2. crud operations need to be extended by the repository

@Repository
public interface userRepository extends JpaRepository<UserTbl, Integer> {

}




