package com.example.Springcrudappproject.repositry;

import com.example.Springcrudappproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User,Long>, CrudRepository<User, Long> {
    User findByEmail(String email);

    @Override
    void deleteById(@NonNull Long id);

    Optional<User> findById(long id);

//    User findById(int id);
    List<User> findByEmailContaining(String str);
}
