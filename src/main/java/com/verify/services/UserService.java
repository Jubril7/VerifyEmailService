package com.verify.services;

import com.verify.entity.Users;
import com.verify.repo.UserRepo;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> saveUser(Users users);

    ResponseEntity<?> confirmEmail(String confirmationToken);

}
