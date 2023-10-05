package com.example.Springcrudappproject.service;

import com.example.Springcrudappproject.Dto.UserDto;
import com.example.Springcrudappproject.model.User;

import java.util.List;

public interface UserService {
    User saveOrUpdate(Long id, UserDto userDto);
    User newRegister( UserDto userDto);



    List<User> getAllUsers();
    User getUserById(Long id);
    void  deleteUserById(Long id);
    //String saveUpdatedStudent(UserDto userDto,Long id);

}
