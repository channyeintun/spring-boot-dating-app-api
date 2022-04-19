package com.pledge.app.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class RandomNumber {
    public static int generate(int upperbound){
        Random rand = new Random();
        log.info("upperbound {}",upperbound);
        int num=rand.nextInt(upperbound);
        log.info("random result {}",num);
        return num;
    }
}
