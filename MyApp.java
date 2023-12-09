///usr/bin/env jbang "$0" "$@" ; exit $?

//DEPS org.slf4j:slf4j-api:1.7.35
//DEPS org.slf4j:slf4j-simple:1.7.35
//DEPS com.pi4j:pi4j-core:2.3.0
//DEPS com.pi4j:pi4j-plugin-raspberrypi:2.3.0
//DEPS com.pi4j:pi4j-plugin-pigpio:2.3.0

//SOURCES Laser.java
//SOURCES Alien.java
//SOURCES Hero.java

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
import com.pi4j.Pi4J;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;

public class MyApp extends JComponent {
    static final int alienHeight = 25;    //variable for the height of the alien
    static final int alienWidth = 75;     //variable for the width of the alien
    static final int laserY1 = 740;       //variable for the 1st y position of the laser
    static final int laserY2 = 760;       //variable for the 2nd y position of the laser
    static int score = 0;                //variable for the score of the game initialized at 0          
    ArrayList<Alien> alienList = new ArrayList<Alien>();    //Array for keeping track of the aliens
    ArrayList<Laser> laserList = new ArrayList<Laser>();    //Array for keeping track of the lasers
    Hero hero = new Hero(40, 100, 450,760);    //initializes new hero
    
    //action listener for the timer for the aliens and which direction they are moving in
    ActionListener taskListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int x, y, xOffset = 0, yOffset = 0;

            //if the last move was to the right check and see if they are up against the edge 
            //if it is against the edge, move it down, otherwise continue to move it to the right
            if(Alien.getLastMove().equals("right")){
                if(alienList.get(20).getX() >= 890){
                    yOffset = 10;
                    Alien.setLastMove("downRight");
                }
                else{
                    xOffset = 10;
                }
            }
            //if the last move was to the left check and see if they are up against the edge 
            //if it is against the edge, move it down, otherwise continue to move it to the left
            else if(Alien.getLastMove().equals("left")){
                if(alienList.get(0).getX() <= 10){
                    yOffset = 10;
                    Alien.setLastMove("downLeft");
                }
                else {
                    xOffset = -10;
                }
            }
            //if the last move was down and the one before was to the left, move it the right
            else if(Alien.getLastMove().equals("downLeft")){
                xOffset = 10;
                Alien.setLastMove("right");
            }
            //if the last move was down and the one before was to the right, move it to the left
            else if(Alien.getLastMove().equals("downRight")){
                xOffset = -10;
                Alien.setLastMove("left");
            }

            //for each alien, adjust the x and y values based on the offsets from which direction they should be moving in
            for(Alien alien: alienList){
                x = alien.getX();
                x += xOffset;
                alien.setX(x);

                y = alien.getY();
                y += yOffset;
                alien.setY(y);
            }

            //repaint the screen
            repaint();
        }
    };

    //Action object for moving the hero to the left
    Action moveHeroLeft = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int x;

            //gets the previous x value for the hero
            x = hero.getX();
            //adjusts the x value by -10 to move the left
            x -= 10;

            //checks to ensure that the x value is not off the screen
            if(x >= 0){
                hero.setX(x);
            }

            //repaints the screen
            repaint();
        }
    };

    //Action object for moving the hero to the right
    Action moveHeroRight = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int x;

            //getse the previous x value of the hero
            x = hero.getX();
            //adjusts the value of the x by 10
            x += 10;

            //checks to ensure that the x is not beyond the boundry of the game screen
            if(x + hero.getWidth() <= 1000){
                hero.setX(x);
            }

            //repaints the screen
            repaint();
        }
    };

    //Action object for firing the laser
    Action fireLaser = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int x = hero.getX() + 50; //sets the x position of the laser as just above the hero

            Laser laser = new Laser(x, x, laserY1, laserY2);    //creates a new laser object
            //adds the laser to the array
            if(laserList.isEmpty()){
                laserList.add(0, laser);
            }
            else{
                laserList.add(laserList.size(), laser);
            }

            try{
                //plays the sound for the laser fire
                Runtime.getRuntime().exec("aplay mixkit-laser-cannon-shot-1678.wav");
                
                Thread.sleep(400);

                //blinks the light for the laser fire
                blinkLight(17);
            }catch (Exception exc){
                exc.printStackTrace();
            }
        
            //repaints the screen
            repaint();
        }
    };

    //Action object for moving the laser
    ActionListener laserListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int y1, y2;

            //for each laser = get the y positions, and decrease them by 10 to move them up the screen, and then set the laser position
            for(Laser laser: laserList){
                y1 = laser.getY1();
                y2 = laser.getY2();

                y1 -= 10;
                y2 -= 10;

                laser.setY1(y1);
                laser.setY2(y2);
            }

            //repaints the screen
            repaint();

            //checks to see if a laser has struck an alien
            collisionDetection();
        }
    };

    //Action object for easy difficulty
    Action diffEasy = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            timer1.setDelay(1500);    //sets the delay for the timer at 1.5 seconds
            timer1.start();            //starts the timer
            startStop = true;
            diffLevelSet = true;       
        }
    };

    //Action object for medium difficulty
    Action diffMedium = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            timer1.setDelay(750);    //sets the delay for the timer at 0.75 seconds
            timer1.start();           //starts the timer
            startStop = true;
            diffLevelSet = true;
        }
    };

    Action diffHard = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            timer1.setDelay(300);    //sets the delay for the timer at 0.3 seconds
            timer1.start();        //starts the timer
            startStop = true;
            diffLevelSet = true;
        }
    };

    //Action object for pausing or unpausing the game
    Action pauseUnpause = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //if the game is paused unpause it; else pause the game
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

    //Abstract object for resetting the game
    Action resetGame = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            alienList.clear();    //clears the array of aliens
            laserList.clear();    //clears the array of lasers
            diffLevelSet = false;   //resets the difficulty level
            setupAliens();        //setups the aliens again
            hero.setX(450);        //resets x position of hero
            hero.setY(760);        //resets y position of hero
            score = 0;            //setse score at 0 again

            //repaints the screen
            repaint();            
        }
    };

    public Timer timer1 = new Timer(300, taskListener);        //new Timer object for aliens
    public Timer laserTimer = new Timer(300, laserListener);   //new Timer object for lasers
    private boolean diffLevelSet = false;
    private boolean startStop = false; //false for stop - true for start

    private JPanel panel1;

    public MyApp(){
        //establishes initial positions for aliens on screen
        setupAliens();

        //timer initial delay and start for moving aliens
        timer1.setInitialDelay(500);

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

        //win = 1
        //lose = 0
        //continue = 2
        int finishStatus, count = 0;
        
        try{
            //checks the status of whether the game is over or not
            finishStatus = checkFinish();

            //if the finish status is 1 or the user has won
            if (finishStatus == 1){
                laserTimer.stop();        //stops the laser timer
                timer1.stop();            //stops the alien timer
                g.drawString("CONGRATS YOU WIN", 1020,600);
                g.drawString("Press 0 to play again", 1020, 650);
                
                while(count < 3){
                    blinkLight(22);
                    count++;
                }
            }
            //if the finish status is 0 or the user has lost
            else if(finishStatus == 0){
                laserTimer.stop();        //stops the laser timer
                timer1.stop();            //stops the alien timer
                g.drawString("SORRY YOU LOSE", 1020, 600);
                g.drawString("Press 0 to play again", 1020, 650);
                
                while(count < 3){
                     blinkLight(27);
                    count++;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        //if the difficulty level is not set - display the difficulty levels for the user to select
        if(!diffLevelSet){

            //Input and Action Map for keystroke "1" for easy difficulty
            this.getInputMap().put(KeyStroke.getKeyStroke("1"), "diffEasy");
            this.getActionMap().put("diffEasy", diffEasy);

            //Input and Action Map for keystroke "2" for medium difficulty
            this.getInputMap().put(KeyStroke.getKeyStroke("2"), "diffMedium");
            this.getActionMap().put("diffMedium", diffMedium);

            //Input and Action Map for keystroke "3" for hard difficulty
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

    //setups the initial position and color of the aliens
    public void setupAliens(){
        int x = 150, y = 70;    //initial value for x and y
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA};
        Color color;

        //for loop to modify x and y values for the alien 
        for(int i = 0; i < 5; i++){
            color = colors[i];
            for(int j = 0; j < 5; j++){
                //creates a new alien object and adds it to the array
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

                            //gets the point value of the alien and adds it to the score
                            alienPts = alien.getPtValue();
                            score += alienPts;

                            try {
                                //plays sound for when a collision occurs
                                Runtime.getRuntime().exec("aplay mixkit-arcade-game-explosion-echo-1698.wav");
                                
                                Thread.sleep(400);

                                //blinks lights for when collision occurs
                                blinkLight(27);
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
    
    private void blinkLight(int pin){
        var pi4j = Pi4J.newAutoContext();

        //configuring the led with values using the address as the pin number
        var ledConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("led")
                .name("LED Flasher")
                .address(pin)
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW)
                .provider("pigpio-digital-output");

        //creating a new pi4j object from the led configuration
        var led = pi4j.create(ledConfig);
        
        try{
            led.high();        //setting the led to high or turning on the pin
            Thread.sleep(400); //waiting 0.4 seconds
            led.low();         //setting the led to low or turning off the pin
        }catch(Exception e){
            e.printStackTrace();
        }
        
        pi4j.shutdown();
    }

}
