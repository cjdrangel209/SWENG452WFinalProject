import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class MyApp extends JComponent {
    static final int alienHeight = 25;
    static final int alienWidth = 75;
    static final int laserY1 = 740;
    static final int laserY2 = 760;
    static int score = 0;
    static int timerSeconds;
    String message;
    ArrayList<Alien> alienList = new ArrayList<Alien>();
    ArrayList<Laser> laserList = new ArrayList<Laser>();
    Hero hero = new Hero(40, 100, 450,760);
    ActionListener taskListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int x, y, xOffset = 0, yOffset = 0;

            if(Alien.getLastMove().equals("right")){
                if(alienList.get(20).getX() >= 890){
                    yOffset = 10;
                    Alien.setLastMove("downRight");
                }
                else{
                    xOffset = 10;
                }
            }
            else if(Alien.getLastMove().equals("left")){
                if(alienList.get(0).getX() <= 10){
                    yOffset = 10;
                    Alien.setLastMove("downLeft");
                }
                else {
                    xOffset = -10;
                }
            }
            else if(Alien.getLastMove().equals("downLeft")){
                xOffset = 10;
                Alien.setLastMove("right");
            }
            else if(Alien.getLastMove().equals("downRight")){
                xOffset = -10;
                Alien.setLastMove("left");
            }

            for(Alien alien: alienList){
                x = alien.getX();
                x += xOffset;
                alien.setX(x);

                y = alien.getY();
                y += yOffset;
                alien.setY(y);
            }

            repaint();
        }
    };

    Action moveHeroLeft = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int x;

            x = hero.getX();
            x -= 10;
            hero.setX(x);

            repaint();
        }
    };

    Action moveHeroRight = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int x;

            x = hero.getX();
            x += 10;
            hero.setX(x);

            repaint();
        }
    };

    Action fireLaser = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int x = hero.getX() + 50;

            Laser laser = new Laser(x, x, laserY1, laserY2);
            if(laserList.isEmpty()){
                laserList.add(0, laser);
            }
            else{
                laserList.add(laserList.size(), laser);
            }

            try{

                String filePath = "C:/Users/cjdra/Documents/Penn State/SWENG 452W/Final Project/mixkit-laser-cannon-shot-1678.wav";
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));

                Clip clip = AudioSystem.getClip();

                clip.open(audioInputStream);
                clip.start();

                Thread.sleep(300);

                clip.stop();
                clip.close();
                audioInputStream.close();
            }catch (Exception exc){
                exc.printStackTrace();
            }


            repaint();
        }
    };

    ActionListener laserListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int y1, y2;

            for(Laser laser: laserList){
                y1 = laser.getY1();
                y2 = laser.getY2();

                y1 -= 10;
                y2 -= 10;

                laser.setY1(y1);
                laser.setY2(y2);
            }

            repaint();

            collisionDetection();
        }
    };

    Action diffEasy = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            timer1.setDelay(1500);
            timer1.start();
            diffLevelSet = true;
        }
    };

    Action diffMedium = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            timer1.setDelay(750);
            timer1.start();
            startStop = true;
            diffLevelSet = true;
        }
    };

    Action diffHard = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            timer1.setDelay(300);
            timer1.start();
            startStop = true;
            diffLevelSet = true;
        }
    };

    Action pauseUnpause = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!startStop){
                timer1.start();
                laserTimer.start();
                startStop = true;
            }
            else{
                timer1.stop();
                laserTimer.stop();
                startStop = false;
            }
        }
    };

    Action resetGame = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            alienList.clear();
            laserList.clear();
            diffLevelSet = false;
            setupAliens();
            hero.setX(450);
            hero.setY(760);
            score = 0;

            repaint();
        }
    };

    public Timer timer1 = new Timer(300, taskListener);
    public Timer laserTimer = new Timer(300, laserListener);
    private boolean diffLevelSet = false;
    private boolean startStop = false; //false for stop - true for start


    private JPanel panel1;

    public MyApp(){
        //establishes initial positions for aliens on screen
        setupAliens();

        //timerSeconds = hardnessLevel();



        //timer initial delay and start for moving aliens
        timer1.setInitialDelay(500);
        //timer1.start();

        //Input and Action Map for keystroke "A" for moving hero to the left
        this.getInputMap().put(KeyStroke.getKeyStroke("A"), "moveLeft");
        this.getActionMap().put("moveLeft", moveHeroLeft);

        //Input and Action Map for keystroke "D" for moving hero the right
        this.getInputMap().put(KeyStroke.getKeyStroke("D"), "moveRight");
        this.getActionMap().put("moveRight", moveHeroRight);

        //Input and Action Map for keystroke "SPACE" for firing the laser
        this.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "fireLaser");
        this.getActionMap().put("fireLaser", fireLaser);

        //Input and Action Map for keystroke "ENTER" for pausing/unpausing game
        this.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pauseUnpause");
        this.getActionMap().put("pauseUnpause", pauseUnpause);

        //Input and Action Map for keystroke "0" for resetting game
        this.getInputMap().put(KeyStroke.getKeyStroke("0"),"resetGame");
        this.getActionMap().put("resetGame", resetGame);

        //start timer for lasers
        laserTimer.start();

    }


    public void paint(Graphics g){
        //draws lines to separate game play screen from score and message panels on side
        g.setColor(Color.YELLOW);
        g.drawLine(1000, 0,1000, 850);      //straight line down
        g.drawLine(1000, 150,1350, 150);    //straight line across

        //draw the aliens
        for (Alien alien : alienList) {

            //draws the aliens as long as they are "visible" and haven't been destroyed
            if(alien.getVisible()) {
                g.setColor(alien.getColor());
                g.fillRect(alien.getX(), alien.getY(), alienWidth, alienHeight);
            }
        }

        //draw the hero
        g.setColor(Color.WHITE);
        g.fillRect(hero.getX(), hero.getY(), hero.getWidth(), hero.getHeight());

        //draw the lasers
        for(Laser laser: laserList){
            if(laser.getVisible()){
                g.setColor(Color.ORANGE);

                g.drawLine(laser.getX1(), laser.getY1(),laser.getX2(), laser.getY2());
            }
        }

        //displaying the score
        g.setColor(Color.YELLOW);
        g.setFont(new Font("OCR A Extended", Font.PLAIN, 24));
        g.drawString("SCORE:", 1130, 50);
        g.drawString(Integer.toString(score),1150, 100);

        g.setFont(new Font("OCR A Extended", Font.PLAIN, 16));
        //g.drawString(message, 1030, 250);

        //win = 1
        //lose = 0
        //continue = 2
        int finishStatus;

        finishStatus = checkFinish();
        if (finishStatus == 1){
            laserTimer.stop();
            timer1.stop();
            g.drawString("CONGRATS YOU WIN", 1020,600);
            g.drawString("Press 0 to play again", 1020, 650);
        }
        else if(finishStatus == 0){
            laserTimer.stop();
            timer1.stop();
            g.drawString("SORRY YOU LOSE", 1020, 600);
            g.drawString("Press 0 to play again", 1020, 650);
        }

        if(!diffLevelSet){

            this.getInputMap().put(KeyStroke.getKeyStroke("1"), "diffEasy");
            this.getActionMap().put("diffEasy", diffEasy);

            this.getInputMap().put(KeyStroke.getKeyStroke("2"), "diffMedium");
            this.getActionMap().put("diffMedium", diffMedium);

            this.getInputMap().put(KeyStroke.getKeyStroke("3"), "diffHard");
            this.getActionMap().put("diffHard", diffHard);

            g.drawString("Please select a difficulty level:", 1020,200);
            g.drawString("1.) Easy", 1020, 220);
            g.drawString("2.) Medium", 1020, 240);
            g.drawString("3.) Hard", 1020, 260);
        }

        g.drawString("Press 'A' key to move left", 1020, 310);
        g.drawString("Press 'D' key to move right", 1020, 330);
        g.drawString("Press SPACE to fire laser", 1020,350);
        g.drawString("Press ENTER to pause/unpause game", 1020,370);

    }

    public void setupAliens(){
        int x = 150, y = 70;
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA};
        Color color;
        for(int i = 0; i < 5; i++){
            color = colors[i];
            for(int j = 0; j < 5; j++){
                Alien alien = new Alien(x, y, color, true);
                alienList.add(i + j, alien);
                
                x += 130;

            }
            x = 150;
            y += 90;

            if(i % 2 == 0){
                x += 50;
            }
        }
    }

    public void collisionDetection(){
        int laserTop, alienBottom, alienPts;

        //for loop checks all of the lasers in the array
        for(Laser laser: laserList){
            //if a laser is listed as "visible"
            if(laser.getVisible()){
                //if it is visible, looks at all of the aliens in the array
                for(Alien alien: alienList){
                    //if the alien is listed as "visible"
                    if(alien.getVisible()){
                        laserTop = laser.getY1();
                        alienBottom = alien.getY() + alienHeight;

                        //checks if the top of the laser is less than or equal to the bottom of the alien
                        //and then checks if the laser is within the x positions of the left edge and right
                        //edge of the alien
                        if((laserTop <= alienBottom) && (laserTop >= alienBottom - alienHeight)
                                && (laser.getX1() >= alien.getX()) &&
                                (laser.getX1() <= (alien.getX() + alienWidth) )){
                            //changes the laser and the alien to be no longer "visible"
                            laser.setVisible(false);
                            alien.setVisible(false);

                            alienPts = alien.getPtValue();
                            score += alienPts;

                            try {
                                String filePath = "C:/Users/cjdra/Documents/Penn State/SWENG 452W/Final Project/mixkit-arcade-game-explosion-echo-1698.wav";
                                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));

                                Clip clip = AudioSystem.getClip();
                                clip.open(audioInputStream);
                                clip.start();
                                Thread.sleep(400);
                                clip.stop();
                                clip.close();
                                audioInputStream.close();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private int checkFinish(){
        //win = 1
        //lose = 0
        //continue = 2
        int finished = 2;
        int count = 0;

        //checks to see if any of the aliens are still visible
        //or if all of the aliens have been hit
        for(Alien alien: alienList){
            if(!alien.getVisible()){
                count++;
            }
            if (count == alienList.size()){
                finished = 1;
            }
        }

        //checks if an alien has reached the bottom
        //if the alien is visible, checks the relative x position of the "bottom"
        for(Alien alien: alienList){
            if (alien.getVisible()){
                if((alien.getY() + alienHeight) >= hero.getY()){
                    finished = 0;
                }
            }
        }

        return finished;
    }

}
