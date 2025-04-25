package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public Map<String, Object> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());

        return createResponse("users", users);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
        return createSingleResponse("users", userDTO);
    }

    @PostMapping
    public Map<String, Object> createUser(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("username");
        String email = (String) request.get("email");

        User user = new User();
        user.setUsername(name);
        user.setEmail(email);

        User savedUser = userService.saveUser(user);
        UserDTO userDTO = new UserDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
        return createSingleResponse("users", userDTO);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        User user = userService.getUserById(id);

        if (request.containsKey("username")) {
            user.setUsername((String) request.get("username"));
        }
        if (request.containsKey("email")) {
            user.setEmail((String) request.get("email"));
        }

        User updatedUser = userService.saveUser(user);
        UserDTO userDTO = new UserDTO(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail());
        return createSingleResponse("users", userDTO);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return response;
    }

    private Map<String, Object> createResponse(String type, List<UserDTO> data) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> formattedData = data.stream()
                .map(user -> Map.of(
                        "type", type,
                        "id", user.id().toString(),
                        "attributes", Map.of(
                                "name", user.username(),
                                "email", user.email()
                        )
                ))
                .collect(Collectors.toList());
        response.put("data", formattedData);
        return response;
    }

    private Map<String, Object> createSingleResponse(String type, UserDTO userDTO) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", Map.of(
                "type", type,
                "id", userDTO.id().toString(),
                "attributes", Map.of(
                        "name", userDTO.username(),
                        "email", userDTO.email()
                )
        ));
        return response;
    }
}
