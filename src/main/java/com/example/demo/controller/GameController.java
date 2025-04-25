package com.example.demo.controller;

import com.example.demo.dto.GameMoveParam;
import com.example.demo.service.GameService;
import com.example.demo.dto.GameCreationParams;
import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import fr.le_campus_numerique.square_games.engine.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;


@RestController
public class GameController {

    @Autowired
    GameService gameService;

    //Créer une partie
    @PostMapping("/games")
    public Game createGame(@RequestBody GameCreationParams params) throws IllegalArgumentException {
        return gameService.createGame(params.typeGame(), params.playerCount(), params.boardSize());
    }

    //Ressortir toutes les parties par une liste de leur ID de GameFactory not ok, a corriger
    @GetMapping("/games/AllGames")
    public Stream<Game> allGames() {
        return gameService.getGameIdentifiers();
    }

    //Afficher une partie par son Id ok
    @GetMapping("/games/{gameId}")
    public Object getGame(@PathVariable String gameId) {
        return gameService.getGame(gameId);
    }

    //Supprimer une partie ok
    @DeleteMapping("/delete/{gameId}")
    public void deleteGame(@PathVariable String gameId) {
        gameService.deleteGame(gameId);
    }

    //Afficher toutes les parties ok
    @GetMapping("/games")
    public List<Game> getGames() {
        return gameService.displayGame();
    }

    //Retourner tous les tokens ok
    @GetMapping("/games/{gameId}/tokens")
    public List<Token> getTokens(@PathVariable String gameId) {
        try {
            return gameService.getTokens(gameId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Jouer un coup
    @PutMapping("/games/{gameId}/tokens/{tokenId}/positions")
    public void moveToken(@PathVariable String gameId, @PathVariable String tokenId, @RequestBody GameMoveParam param) {
        try {
            gameService.move(gameId, tokenId, param.x(), param.y());
        } catch (InvalidPositionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    //Ressortir les parties en cours
    @GetMapping("/games/filter")
    public List<Game> filterGames() {
        return gameService.currentStatus();
    }

    //Connaître la liste des coups possibles
    @GetMapping("/games/{gameId}/token/{tokenName}/possibilities")
    public Collection<CellPosition> possibilityMoves(@PathVariable String gameId, @PathVariable String tokenName, @RequestBody GameMoveParam param) {
        return gameService.possibilityMoves(gameId, tokenName, param.x(), param.y());
    }


}

