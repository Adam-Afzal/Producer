package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
@Component
public class Avatar {


    public Long generateSeed(){
        Long seedNumber = (long) new Random().nextInt(2000);
        return seedNumber;

    }
}
