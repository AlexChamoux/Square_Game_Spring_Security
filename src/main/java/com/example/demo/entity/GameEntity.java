package com.example.demo.entity;


import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.List;

@Setter
@Getter

@Entity
@Table(name = "game")
public class GameEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String factoryId;

    @Column(nullable = false)
    private int boardSize;

    @Column(nullable = false)
    private String playerIds;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameTokenEntity> tokens;
}
