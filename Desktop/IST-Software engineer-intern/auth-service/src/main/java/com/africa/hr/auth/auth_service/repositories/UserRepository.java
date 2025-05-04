package com.africa.hr.auth.auth_service.repositories;



import com.africa.hr.auth.auth_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    List<User> findByDepartment(String department);

    List<User> findByEmailIn(List<String> emails);


}
