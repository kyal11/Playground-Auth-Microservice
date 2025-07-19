package com.microservice.users.domain.repository;

import com.microservice.users.domain.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Page<Users> findAllByDeletedAtIsNull(Pageable pageable);

    @Query("SELECT u FROM Users u WHERE u.deletedAt IS NULL")
    List<Users> findAllUsers();

    Optional<Users> findById(Long id);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByProviderId(String providerId);

    @Modifying
    @Query("UPDATE Users u SET u.deletedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void softDeleteById(Long id);

    @Modifying
    @Query("UPDATE Users u SET u.password = :password WHERE u.id = :id")
    void updatePassword(Long id, String password);

    Users save(Users user);
}
