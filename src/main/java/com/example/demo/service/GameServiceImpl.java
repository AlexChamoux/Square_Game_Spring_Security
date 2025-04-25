package com.example.demo.service;

import com.example.demo.plugin.GamePlugin;
import fr.le_campus_numerique.square_games.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    GameDao gameDao;

    @Autowired
    List<GamePlugin> plugins;


    @Override
    public Game createGame(String typeGame, int playerCount, int boardSize) throws IllegalArgumentException {
        GamePlugin plugin = plugins.stream()
                .filter(p -> p.getDefaultTypeGame().equals(typeGame))
                .findFirst()
                .orElse(null);

        if (plugin == null) {
            throw new IllegalArgumentException("No plugin found for the game type: " + typeGame);
        }

        Game game = plugin.getGameFactory().createGame(plugin.getDefaultPlayerCount(), plugin.getDefaultBoardSize());
        gameDao.save(game);
        return game;
    }

    @Override
    public Stream<Game> getGameIdentifiers(){
        return plugins.stream()
                .map(plugin -> plugin.getGameName(getLocale(), plugin.getDefaultTypeGame()))
                .map(gameDao.getDataGames()::get)
                .filter(Objects::nonNull);
    }

    @Override
    public Game getGame(String gameId) {
        return gameDao.findById(gameId).orElse(null);
    }

    @Override
    public void deleteGame(String gameId){
        gameDao.delete(gameId);
    }

    @Override
    public List<Game> displayGame(){
        List<Game> games = new ArrayList<>();
        games.addAll(gameDao.getDataGames().values());
        return games;
    }

    private static Token getTokenWithName(Game game, String tokenName) {
        return Stream.of(game.getRemainingTokens(), game.getRemovedTokens(), game.getBoard().values())
                .flatMap(Collection::stream)
                .filter(t -> t.getName().equals(tokenName))
                .filter(t -> t.canMove())
                .findFirst()
                .orElse(null);
    }

    private static List<Token> getAllTokens(Game game) {
        return Stream.of(game.getRemainingTokens(), game.getRemovedTokens(), game.getBoard().values())
                .flatMap(Collection::stream)
                .filter(t -> t.canMove())
                .collect(Collectors.toList());
    }

    public List<Token> getTokens(String gameId) throws IllegalArgumentException {
        return getAllTokens(gameDao.getDataGames().get(gameId));
    }

    public void move(String gameId, String tokenId, int x, int y) throws InvalidPositionException {
        Token token = getTokenWithName(gameDao.getDataGames().get(gameId), tokenId);
        token.moveTo(new CellPosition(x, y));
    }

    @Override
    public List<Game> currentStatus() {
        return gameDao.getDataGames().values().stream()
                .filter(game -> game.getStatus() == GameStatus.ONGOING)
                .collect(Collectors.toList());
    }

    public Collection<CellPosition> possibilityMoves(String gameId, String tokenName, int x, int y){
        Token token = getTokenWithName(gameDao.getDataGames().get(gameId), tokenName);

        return token.getAllowedMoves();
    }

}
