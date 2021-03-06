package com.task.booking.controllers;

import com.task.booking.models.Role;
import com.task.booking.models.User;
import com.task.booking.repo.UserRepository;
import com.task.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
public class UserController {
    //Контроллер, отвечающий за пользователя: вывод списка пользователей, редактирования/удаление пользователя

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/user")
    public String usesrList(Model model){
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping ("/user-edit/{id}")
    public String editUser(@PathVariable (value="id") Long id, Model model){
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/user-edit/{id}")
    public String editPostUser (@PathVariable (value="id") Long id, @RequestParam String username,
                                @RequestParam (name="roles[]", required = false) String [] roles){
        if (!userRepository.existsById(id)) {
            return "redirect:/user";
        }
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(username);

        if(roles!=null) {
            Arrays.stream(roles).forEach(r -> user.getRoles().add(Role.valueOf(r)));
        }

        userRepository.save(user);
        return "redirect:/user";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping ("user-remove/{id}")
    public String removePostUser (@PathVariable (value ="id") Long id){
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
        return "redirect:/user";
    }

}
