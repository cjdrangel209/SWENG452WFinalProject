public class Hero {
    private int height;    //variable for the height of the hero
    private int width;     //variable for the width of the hero
    private int x;        //variable for x position of hero
    private int y;        //variable for y position of hero

    //Hero constructor
    public Hero(int h, int w, int x, int y){
        this.height = h;
        this.width = w;
        this.x = x;
        this.y = y;
    }

    //sets the height of hero
    public void setHeight(int h){
        this.height = h;
    }

    //gets the height of hero
    public int getHeight(){
        return height;
    }

    //sets the width of hero
    public void setWidth(int w){
        this.width = w;
    }

    //gets the width of hero
    public int getWidth(){
        return width;
    }

    //sets x position of hero
    public void setX(int x){
        this.x = x;
    }

    //gets x position of hero
    public int getX(){
        return x;
    }

    //sets y position of hero
    public void setY(int y){
        this.y = y;
    }

    //gets y position of hero
    public int getY(){
        return y;
    }

}
