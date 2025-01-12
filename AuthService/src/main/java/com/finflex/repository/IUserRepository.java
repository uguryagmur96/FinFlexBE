package com.finflex.repository;

import com.finflex.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    Optional<User> findOptionalByTckn(String tckn);
    Optional<User> findOptionalByUserName(String userName);
    Optional<User> findOptionalByMailAddress(String mailAddress);
    Optional<User> findOptionalByPersonelNumber(String personelNumber);
    Optional<User> findOptionalById(Long id);

}
