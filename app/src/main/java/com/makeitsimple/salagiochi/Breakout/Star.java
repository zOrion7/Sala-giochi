package com.makeitsimple.salagiochi.Breakout;

import java.util.Random;

public class Star {

    private int x;
    private int y;
    private int speed;

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;

    public Star(int screenX, int screenY){

        maxX = screenX;
        maxY = screenY;

        minX=0;
        minY=0;

        Random generator = new Random();
        speed = generator.nextInt(4) + 2;

        // Randomize coordinates
        x = generator.nextInt(screenX);
        y = generator.nextInt(screenY);
    }

    public void update(){

        y+= speed;

        // If a star leaves the screen, it restarts from top
        if(y>maxY){
            y=0;
            Random generator = new Random();
            x = generator.nextInt(maxX);
            speed = generator.nextInt(4) + 2;
        }
    }


    // Randomize star's size

    public float getStarSize(){

        float minX =1.0f;
        float maxX = 5.0f;

        Random rand = new Random();

        return rand.nextFloat()*(maxX - minX)+minX;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

}
