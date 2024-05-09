package main;

import entity.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable{
    //Screen Setting
    final int originalTileSize = 16;   //16x16
    final int scale = 4;
    public final int tileSize = originalTileSize * scale;  //64x64
    final int maxScreenCol = 10;
    final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize;  //576
    public final int screenHeight = maxScreenRow * tileSize; //768


    //FPS
    public int FPS = 60;
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;

    //Class
    public Player player;
    UI ui = new UI(this);
    public Enemy yuyuko = new Enemy(this);
    FirstNon firstNon = new FirstNon(this);
    Projectile pr = new Projectile(this);
    BackgroundManager bg = new BackgroundManager(this);
    Sound se = new Sound();
    Sound bgm = new Sound();


    //Player position
    int PlayerX = 100;
    int PlayerY = 100;
    int PlayerSpeed = 6;


    //Game State
    public int gameState;
    public int titleState = 0;
    public final int playState = 1;
    public final int tutorialState = 2;
    public final int musicState = 3;

    public int section = 1;
    public final int firstNonSection = 1;
    public final int finalSpellSection = 2;

    public static int numberOfBullets = 0;
    public static int time_left = 3600;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        setup();
    }

    public void setup(){
        gameState = titleState;
        playMusic(5);
    }

    public void startGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){
        while (gameThread != null){
            double drawInterval = 1000000000/FPS;
            double nextDrawTime = System.nanoTime() + drawInterval;
            update();
            //System.out.println(player.x + "," + player.y);
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(){
        numberOfBullets = 0;

        if (gameState == playState){
            if (section == firstNonSection) {
                yuyuko.setfanVisible(true);
                firstNon.update();
            }
            if (section == finalSpellSection) {
                yuyuko.setfanVisible(false);
                pr.update();
            }

            bg.update();
            yuyuko.update();
            player.update();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //TITLE
        if (gameState == titleState){
            ui.draw(g2d);
        }

        if (gameState == playState){
            bg.draw(g2d);
            yuyuko.draw(g2d);
            player.draw(g2d);
            if (section == firstNonSection) firstNon.draw(g2d);
            if (section == finalSpellSection) pr.draw(g2d);
        }

        g2d.dispose();
    }

    public void playMusic(int i){
        bgm.setFile(i);
        bgm.play();
        bgm.loop();
    }

    public void stopMusic(){
        bgm.stop();
    }

    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
}
