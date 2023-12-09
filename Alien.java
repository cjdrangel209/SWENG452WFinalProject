import java.awt.*;
public class Alien {
    private int x;    //variable for tracking x position of alien
    private int y;    //variable for tracking y position of alien
    private Color color;    //variable for color displayed of alien
    private boolean visible;    //variable tracking whether the alien was struck or not
    private int ptValue;        //variable for point value assigned to alien
    private static String lastMove = "right";    //class variable for last direction the alien moved in

    //Alien constructor
    public Alien(int x, int y, Color color, boolean visible){
        this.x = x;
        this.y = y;
        this.color = color;
        this.visible = visible;
        setPtValue();
    }

    //sets x value for alien
    public void setX(int x){
        this.x = x;
    }

    //gets x value for alien
    public int getX(){
        return this.x;
    }

    //sets y value for alien
    public void setY(int y){
        this.y = y;
    }

    //gets y value for alien
    public int getY(){
        return this.y;
    }

    //sets color for alien
    public void setColor(Color color){
        this.color = color;
    }

    //getse color of the alien
    public Color getColor(){
        return this.color;
    }

    //sets whether the alien is visible or not
    public void setVisible(boolean visible){
        this.visible = visible;
    }

    //gets visible value for the alien
    public boolean getVisible(){
        return this.visible;
    }

    //sets the last move class variable
    public static void setLastMove(String move){
        lastMove = move;
    }

    //gets the last move value
    public static String getLastMove(){
        return lastMove;
    }

    //function to set the point value of the alien based on the color
    private void setPtValue(){
        if(color.equals(Color.RED)){
            ptValue = 100;
        }
        else if(color.equals(Color.BLUE)){
            ptValue = 70;
        }
        else if(color.equals(Color.GREEN)){
            ptValue = 50;
        }
        else if(color.equals(Color.YELLOW)){
            ptValue = 30;
        }
        else if(color.equals(Color.MAGENTA)){
            ptValue = 10;
        }
    }

    //gets the point value for the alien
    public int getPtValue(){
        return this.ptValue;
    }
}
