package com.verify.repo;

import com.verify.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    Users findByUserEmailIgnoreCase(String email);

    Boolean existsByUserEmail(String email);

}
