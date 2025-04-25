package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomHeartbeat implements HeartbeatSensor {

    @Override
    public int get() {
        Random rand = new Random();

        return rand.nextInt(190) + 40;
    }
}
