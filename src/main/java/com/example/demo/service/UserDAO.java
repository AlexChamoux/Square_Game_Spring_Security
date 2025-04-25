package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.List;

public interface UserDAO {
    public List<User> getAllUsers();
    public User getUserById(int id);
    public void addUser(User user);
    public void updateUser(User user);
    public void deleteUser(int id);
}