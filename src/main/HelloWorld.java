package main;

import engine.IGameLogic;
import engine.Engine;
import game.DemoGame;

public class HelloWorld {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DemoGame();
            Engine engine = new Engine("AndromedaEngine", 1200, 960, vSync, gameLogic);
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}