//package com.example.demo.service;
//
//import fr.le_campus_numerique.square_games.engine.Game;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//public class InMemoryGameDao implements GameDao {
//
//    Map<String, Game> dataGames = new HashMap<>();
//
//    public Map<String, Game> getDataGames(){
//        return dataGames;
//    }
//
//    @Override
//    public Optional<Game> findById(String gameId) {
//        return Optional.ofNullable(dataGames.get(gameId));
//    }
//
//    @Override
//    public void delete(String gameId){
//        dataGames.remove(gameId);
//    }
//
//    @Override
//    public void save(String id, Game game) {
//        dataGames.put(id, game);
//    }
//
//}
