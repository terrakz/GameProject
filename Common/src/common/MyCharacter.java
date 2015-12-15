package common;

import java.awt.*;
import java.io.Serializable;


public class MyCharacter implements Serializable {
    
    private String name;
    private Image i;
    private int x;
    private int y;
    
    public MyCharacter(String name, Image i){
        this.name = name;
        this.i = i;
    }
    public MyCharacter(){
        this.name = "Default";
        this.x = 100;
        this.y = 100;
    }
    public MyCharacter(int x, int y){
        this.name = "Default";
        this.x = x;
        this.y = y;
    }
    
    public String getName(){
        return name;
    }
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    
    public void Up(){
        y -= 5;
    }
    public void Down(){
        y += 5;
    }
    public void Left(){
        x -= 5;
    }
    public void Right(){
        x += 5;
    }
}
