public class Hero {
    private int height;
    private int width;
    private int x;
    private int y;

    public Hero(int h, int w, int x, int y){
        this.height = h;
        this.width = w;
        this.x = x;
        this.y = y;
    }

    public void setHeight(int h){
        this.height = h;
    }

    public int getHeight(){
        return height;
    }

    public void setWidth(int w){
        this.width = w;
    }

    public int getWidth(){
        return width;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getX(){
        return x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getY(){
        return y;
    }

}
