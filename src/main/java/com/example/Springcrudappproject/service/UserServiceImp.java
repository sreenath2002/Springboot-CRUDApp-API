package com.example.Springcrudappproject.service;

import com.example.Springcrudappproject.Dto.UserDto;
import com.example.Springcrudappproject.model.User;
import com.example.Springcrudappproject.repositry.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImp implements UserService{
   // private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
// private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
 @Autowired
 private PasswordEncoder passwordEncoder;
 @Autowired
 private UserRepository userRepository;

    public User newRegister( UserDto userDto)
    {
        User user = new User(userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()), "USER", userDto.getFullname());
        userRepository.save(user);
        return user;
    }

    @Override

    public User saveOrUpdate(Long id, UserDto userDto) {
//

        User userFromDB = userRepository.findById(id).get();
//        if (userFromDB == null) {
//
//            User user = new User(userDto.getEmail(),passwordEncoder.encode(userDto.getPassword()), "USER", userDto.getFullname());
//            userRepository.save(user);
//            return user;
//        }


        if (userDto.getEmail() != null) {
            userFromDB.setEmail(userDto.getEmail());

        }
        if (userDto.getFullname() != null) {
            userFromDB.setFullname(userDto.getFullname());
        }
        if (userDto.getPassword() != null) {
            userFromDB.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userRepository.save(userFromDB);
        return userFromDB;
    }



    @Override
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }
    @Override
    public User getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    @Override
    public void deleteUserById(Long id)
    {
        this.userRepository.deleteById(id);
    }


}
