package com.makeitsimple.salagiochi.FlappyPlanet;

import java.util.Random;

public class Star {

    private int x;
    private int y;
    private int speed;

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;

    private static int MAX_SPEED = 8;

    public Star(int screenX, int screenY){

        maxX = screenX;
        maxY = screenY;

        minX=0;
        minY=0;

        Random generator = new Random();
        speed = generator.nextInt(10);

        //genero coordinate casuali all'interno dello schermo
        x = generator.nextInt(screenX);
        y = generator.nextInt(screenY);
    }

    public void update(int playerSpeed){


        if(playerSpeed>MAX_SPEED)playerSpeed=MAX_SPEED;
        x-= playerSpeed;
        x-= speed;



        //se la stella raggiunge la parte sinistra dello schermo, torna alla parte destra.
        if(x<0){
            x=maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);
            speed = generator.nextInt(15);
        }
    }


    //rende la larghezza della stella casuale

    public float getStarWidth(){

        float minX =1.0f;
        float maxX = 5.0f;

        Random rand = new Random();

        float finalX = rand.nextFloat()*(maxX - minX)+minX;
        return finalX;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

}
