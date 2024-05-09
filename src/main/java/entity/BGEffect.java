package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BGEffect {
    GamePanel gp;
    BufferedImage img;
    double x, y, speed;
    int type;

    public BGEffect(GamePanel gp){
        this.speed = 6;
        this.gp = gp;
        try {
            img = ImageIO.read(getClass().getResourceAsStream("/player/spellcard_bg.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.x = 0;
        this.y = gp.screenHeight;
    }

    public void setPos(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void update(){
        y -= speed;
    }

    public void draw(Graphics2D g2d){
        g2d.drawImage(img,(int)x,(int)y, gp.screenWidth, gp.screenHeight,null);
    }
}
