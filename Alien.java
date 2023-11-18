import java.awt.*;
public class Alien {
    private int x;
    private int y;
    private Color color;
    private boolean visible;
    private int ptValue;
    private static String lastMove = "right";

    public Alien(int x, int y, Color color, boolean visible){
        this.x = x;
        this.y = y;
        this.color = color;
        this.visible = visible;
        setPtValue();
    }

    public void setX(int x){
        this.x = x;
    }

    public int getX(){
        return this.x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getY(){
        return this.y;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public boolean getVisible(){
        return this.visible;
    }

    public static void setLastMove(String move){
        lastMove = move;
    }

    public static String getLastMove(){
        return lastMove;
    }

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

    public int getPtValue(){
        return this.ptValue;
    }
}
