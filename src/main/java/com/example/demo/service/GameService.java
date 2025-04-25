package com.example.demo.service;

import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import fr.le_campus_numerique.square_games.engine.Token;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface GameService {

    Stream<Game> getGameIdentifiers();

    Game createGame(String typeGame, int playerCount, int boardSize) throws IllegalArgumentException;

    Game getGame(String gameId);

    void deleteGame(String gameId);

    List<Game> displayGame();

    void move(String gameId, String tokenId, int x, int y) throws InvalidPositionException;

    List<Game> currentStatus();

    Collection<CellPosition> possibilityMoves(String gameId, String tokenName, int x, int y);

    List<Token> getTokens(String gameId) throws IllegalArgumentException;

}
