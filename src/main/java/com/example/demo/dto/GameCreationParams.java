package com.example.demo.dto;


//DTO
public record GameCreationParams(
        String typeGame,
        int playerCount,
        int boardSize
) {
}
