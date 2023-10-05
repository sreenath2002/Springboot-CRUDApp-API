package com.example.Springcrudappproject.controller;

import java.security.Principal;
import java.util.List;

import com.example.Springcrudappproject.Dto.UserDto;
import com.example.Springcrudappproject.model.User;
import com.example.Springcrudappproject.repositry.UserRepository;
import com.example.Springcrudappproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user")UserDto userDto)
    {
       return "register";

    }
    @PostMapping("/registration")
    public String saveUser(@ModelAttribute("user")UserDto userDto, Model model) {
        String email = userDto.getEmail();


        // Regular expression for a basic email format
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (!email.matches(emailRegex)) {
            model.addAttribute("message", "Please provide a valid email address");
            return "register";
        }


        // Check if a user with the provided email already exists
        if (userRepository.findByEmail(email) != null) {
            model.addAttribute("message", "User with this email already exists,Please Login");
            return "register"; // You might want to return to the registration page or handle it differently
        }

        userService.newRegister(userDto);

        model.addAttribute("message", "Registered Successfully");
        return "register";
    }


    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String searchString, Model model) {
        List<User> ls =  userRepository.findByEmailContaining(searchString);
        model.addAttribute("listofUsers", ls);
        return "adminpage";
    }
    @GetMapping("/admin-page")
    public String adminpage(Model model,Principal principal)
    {
        model.addAttribute("listofUsers",userService.getAllUsers());
        return "adminpage";
    }
    @GetMapping("/showNewUserForm")
    public String showNewUserForm(Model model)
    {
        User user = new User();
//        userService.
        model.addAttribute("employee",user);
        return "newUser";
    }
    @PostMapping("/showNewUserForm")
    public String savenewUser( @ModelAttribute("user")UserDto userDto, Model model)
    {
        userService.newRegister(userDto);
        return "redirect:/admin-page";
    }

    @GetMapping("/showFormUpdate")
    public String showFormUpdate(@RequestParam("idval") long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("employee", user);
        return "updateForm";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@RequestParam("idval") long id,@RequestParam ("email") String email, @ModelAttribute("employee") UserDto user,Model model) {
        if(userRepository.findByEmail(user.getEmail())!=null)
        {
            model.addAttribute("message","This Email id already exists");
            return "redirect:/admin-page";
        }

        userService.saveOrUpdate(id, user);
        model.addAttribute("message","Updated succesfully");
        return "redirect:/admin-page";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id)
    {
        userRepository.deleteById(id);
        return "redirect:/admin-page";
    }
    @GetMapping("/user-page")
    public String userpage(Model model,Principal principal)
    {
        UserDetails userDetails=userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user",userDetails);
        return "userpage";
    }
}
