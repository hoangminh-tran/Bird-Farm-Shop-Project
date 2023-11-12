package com.tttm.birdfarmshop.Repository;

import com.tttm.birdfarmshop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(
            value = "select * from `users` where email = ?1", nativeQuery = true
    )
    User findUserByEmail(String email);

    @Query(
            value = "select * from `users` where email = ?1 and password = ?2 and account_status = ?3", nativeQuery = true
    )
    User findUserByEmailAndPassword(String email, String password, String accountStatus);

    @Query(
            value = " select * from `users`", nativeQuery = true
    )
    List<User> getAllUsers();

    @Query(
            value = "select * from `users` where phone = ?1", nativeQuery = true
    )
    User findUserByPhone(String phone);

    @Query(
            value = "select * from `users` where email = ?1 and account_status = ?2", nativeQuery = true
    )
    Optional<User> findUserByEmailAndActiveStatus(String email, String accountStatus);


    @Query(
            value = " select * from `users` where role = ?1", nativeQuery = true
    )
    List<User> getAllUsersBasedOnRole(String role);

}
