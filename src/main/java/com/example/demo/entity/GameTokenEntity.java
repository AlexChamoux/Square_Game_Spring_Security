package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter

@Entity
@Table(name = "game_token")
public class GameTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean removed;

    @Column(nullable = true)
    private Integer x;

    @Column(nullable = true)
    private Integer y;

}
