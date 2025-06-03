package com.app.pandemicnet.repository;

import com.app.pandemicnet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByUniqueId(String uniqueId);
    Optional<User> findUserByUniqueId(String userUniqueId);
    Optional<User> findUserByEmail(String email);
    List<User> findByStatusIn(List<User.Status> statuses);
    List<User> findByPhoneNumberContainingOrEmailContaining(String phoneNumber, String email);
    Boolean existsUserById(Long id);
}