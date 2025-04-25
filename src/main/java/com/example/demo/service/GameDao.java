package com.example.demo.service;

import fr.le_campus_numerique.square_games.engine.Game;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

public interface GameDao{

    Optional<Game> findById(@NotNull String gameId);

    void delete(@NotNull String gameId);

    Map<String, Game> getDataGames();

    void save(Game game);
}
