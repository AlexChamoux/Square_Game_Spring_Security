package com.example.demo.service;

import com.example.demo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserDetailModel implements UserDetails {
    private String name;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserDetailModel(User user){
        this.name = user.getUsername();
        this.password = user.getPassword();
        this.authorities = Stream.of(user.getRoles().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return this.authorities; }

    @Override
    public String getPassword() { return this.password; }

    @Override
    public String getUsername() { return this.name; }

}

