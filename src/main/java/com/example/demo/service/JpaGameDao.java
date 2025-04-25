package com.example.demo.service;

import com.example.demo.entity.GameEntity;
import com.example.demo.entity.GameTokenEntity;
import com.example.demo.plugin.GamePlugin;
import com.example.demo.repository.GameRepository;
import fr.le_campus_numerique.square_games.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class JpaGameDao implements GameDao {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    List<GamePlugin> plugins;


    @Override
    public Optional<Game> findById(String gameId) {
        Optional<GameEntity> result = gameRepository.findById(gameId);

        UUID playerCount = null;
        String factoryId = result.map(GameEntity::getFactoryId).orElse(null);
        if (factoryId != null) {
            playerCount = UUID.fromString(factoryId);
        }

        var boardSize = result.map(GameEntity::getBoardSize).orElse(0);
        var playerIdsOptional = result.map(GameEntity::getPlayerIds);
        List<UUID> players = playerIdsOptional
                .map(ids -> Arrays.stream(ids.split(","))
                        .map(UUID::fromString)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        Optional<List<GameTokenEntity>> tokens = result.map(GameEntity::getTokens);

        Collection<TokenPosition<UUID>> removedTokens = new ArrayList<>();
        Collection<TokenPosition<UUID>> borderTokens = new ArrayList<>();

        tokens.ifPresent(tokenList -> {
            var removedTokensEntity = tokenList.stream()
                    .filter(GameTokenEntity::isRemoved)
                    .toList();

            var borderTokensEntity = tokenList.stream()
                    .filter(b-> !b.isRemoved())
                    .toList();

            removedTokensEntity.forEach(gameTokenEntity -> removedTokens.add(mapToken(gameTokenEntity)));

            borderTokensEntity.forEach(gameTokenEntity -> borderTokens.add(mapToken(gameTokenEntity)));
        });

        GameFactory factory = getGameFactory(factoryId);


        Game game = null;
        try {
            game = factory.createGameWithIds(playerCount, boardSize, players, removedTokens, borderTokens);
        } catch (InconsistentGameDefinitionException e) {
            System.err.println("Erreur : " + e.getMessage());
        }

        return Optional.ofNullable(game);
    }

    private GameFactory getGameFactory(String factoryId) {
        GameFactory factory = plugins.stream()
                .filter(p -> p.getDefaultTypeGame().equals(factoryId)).map(GamePlugin::getGameFactory)
                .findFirst()
                .orElse(null);
        return factory;
    }

    @Override
    public void delete(String gameId) {
        gameRepository.deleteById(gameId);
    }

    public Map<String, Game> getDataGames(){
        List<GameEntity> result = gameRepository.findAll();

        Map<String, Game> gamesMap = new HashMap<>();

        for(GameEntity gameEntity : result){
            String factoryId = gameEntity.getFactoryId();

            GameFactory factory = getGameFactory(factoryId);

            if (factory == null) {
                throw new IllegalArgumentException("No plugin found for the game type: " + factoryId);
            }

            int boardSize = gameEntity.getBoardSize();

            String[] player = gameEntity.getPlayerIds().split(",");
            var players = Stream.of(player).map(UUID::fromString).toList();

            List<GameTokenEntity> tokens = gameEntity.getTokens();
            var removedTokensEntity = tokens.stream().filter(GameTokenEntity::isRemoved).toList();//2 facon d'écrire la même chose, par contre l'un est le contraire de l'autre !
            var borderTokensEntity = tokens.stream().filter(t -> !t.isRemoved()).toList();//2 facon d'écrire la même chose, par contre l'un est le contraire de l'autre !
            Collection<TokenPosition<UUID>> removedTokens = new ArrayList<>();
            removedTokensEntity.forEach(gameTokenEntity -> removedTokens.add(mapToken(gameTokenEntity)));
            Collection<TokenPosition<UUID>> borderTokens = new ArrayList<>();
            borderTokensEntity.forEach(gameTokenEntity -> borderTokens.add(mapToken(gameTokenEntity)));


            try {
                Game game = factory.createGameWithIds(UUID.fromString(gameEntity.getId()), boardSize, players, removedTokens, borderTokens);
                UUID gameId = game.getId();
                String id = gameId.toString();
                gamesMap.put(id, game);
            } catch (InconsistentGameDefinitionException e) {
                System.err.println("Failed to create game: " + e.getMessage());
            }

        }

        return gamesMap;
    }

    @Override
    public void save(Game game) {
        gameRepository.save(map(game));
    }

    private static GameEntity map(Game game) {
        GameEntity gameEntity = new GameEntity();

        gameEntity.setFactoryId(game.getFactoryId());
        gameEntity.setBoardSize(game.getBoardSize());
        gameEntity.setId(game.getId().toString());

        List<GameTokenEntity> tokens = new ArrayList<>();
        game.getRemovedTokens().forEach(token -> tokens.add(mapToken(token)));
        game.getBoard().values().stream().map(JpaGameDao::mapToken).forEach(tokens::add);


        gameEntity.setTokens(tokens);

        String playerIds = game.getPlayerIds().stream()
                .map(UUID::toString)
                .collect(Collectors.joining(","));
        gameEntity.setPlayerIds(playerIds);

        return gameEntity;
    }

    private static GameTokenEntity mapToken(Token token) {
        GameTokenEntity gameTokenEntity = new GameTokenEntity();
        token.getOwnerId().ifPresent(ownerId->gameTokenEntity.setOwnerId(ownerId.toString()));
        gameTokenEntity.setName(token.getName());
        gameTokenEntity.setRemoved(token.canMove());
        gameTokenEntity.setX(token.getPosition().x());
        gameTokenEntity.setY(token.getPosition().y());

        return gameTokenEntity;
    }

    private static TokenPosition<UUID> mapToken(GameTokenEntity gameTokenEntity) {

        return new TokenPosition<>(UUID.fromString(gameTokenEntity.getOwnerId()), gameTokenEntity.getName(), gameTokenEntity.getX(), gameTokenEntity.getY());
    }


}
