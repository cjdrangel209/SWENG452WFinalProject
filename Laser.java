public class Laser {
    private boolean visible;    //variable to track whether laser is visible or not
    private int x1;            //variable for 1st x position
    private int x2;            //variable for 2nd x position
    private int y1;            //variable for 1st y position
    private int y2;            //variable for 2nd y position

    //Laser constructor
    public Laser(int x1, int x2, int y1, int y2){
        this.visible = true;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    //sets the visibility of laser
    public void setVisible(boolean visible){
        this.visible = visible;
    }

    //gets the visibility of laser
    public boolean getVisible(){
        return this.visible;
    }

    //sets first x position
    public void setX1(int x1){
        this.x1 = x1;
    }

    //getse first x position
    public int getX1(){
        return this.x1;
    }

    //sets second x position
    public void setX2(int x2){
        this.x2 = x2;
    }

    //gets second x position
    public int getX2(){
        return this.x2;
    }

    //sets first y position
    public void setY1(int y1){
        this.y1 = y1;
    }

    //gets first y position
    public int getY1(){
        return this.y1;
    }

    //sets second y position
    public void setY2(int y2){
        this.y2 = y2;
    }

    //gets second y position
    public int getY2(){
        return this.y2;
    }
}
