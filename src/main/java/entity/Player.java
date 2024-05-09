package entity;

import main.GamePanel;
import main.KeyHandler;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

import static entity.Hitboxes.checkCollide;

public class Player{
    public double x, y, speed, speedX, speedY;
    public String name;

    public BufferedImage right1, right2, right3, right4, right5;
    public BufferedImage left1, left2, left3, left4, left5;
    public BufferedImage idle1, idle2, idle3, idle4, idle5;
    public BufferedImage img, focusShot;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int cooldown = 1;
    public boolean focusing = false;
    GamePanel gp;
    KeyHandler keyH;

    ArrayList<Shooting> shoot = new ArrayList<>();
    Hitboxes playerHitbox;
    BufferedImage yinyang, hitbox;
    static int miss = 0;
    public int invicible = 0;
    double i = 0;
    double j = 0;
    int timer;

    ArrayList<Hitboxes> hitboxesPool = new ArrayList<>();

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        getPlayerImage();
        setDefaultValue();
    }

    public void setDefaultValue(){
        x = gp.screenWidth/2;
        y = 700;
        playerHitbox = new Hitboxes(x , y, 4);
        speed = 6;
        direction = "idle";
    }

    public void getPlayerImage(){
        try {
            idle1 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_idle1.png"));
            idle2 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_idle2.png"));
            idle3 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_idle3.png"));
            idle4 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_idle4.png"));
            idle5 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_idle5.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_left2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_left3.png"));
            left4 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_left4.png"));
            left5 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_left5.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_right2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_right3.png"));
            right4 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_right4.png"));
            right5 = ImageIO.read(getClass().getResourceAsStream("/player/reimu_right5.png"));
            yinyang = ImageIO.read(getClass().getResourceAsStream("/player/yinyang.png"));
            hitbox = ImageIO.read(getClass().getResourceAsStream("/player/player_hitbox.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void update(){
        for (int i = 0; i < shoot.size(); i++){
            Shooting s = shoot.get(i);
            if (s.destroyed == true || s.x < 0 || s.x > gp.screenWidth || s.y < 0){
                shoot.remove(i);
            }
            else shoot.get(i).update();
        }
        if (invicible > 0) invicible--;
        if (invicible > 4.5*60) return;
        if (invicible == 4.5*60){
            x = gp.screenWidth/2;
            y = 700;
        }
        //move
        if (keyH.upPressed == true && keyH.leftPressed == true){
            direction = "left";
            if (x - speed/Math.sqrt(2) - idle1.getWidth()/2 > 0) x -= (int) (speed/Math.sqrt(2));
            if (y - speed/Math.sqrt(2) - idle1.getHeight()/2 > 0) y -= (int) (speed/Math.sqrt(2));
        }
        else if (keyH.upPressed == true && keyH.rightPressed == true){
            direction = "right";
            if (x + speed/Math.sqrt(2) + idle1.getWidth()/2 < gp.screenWidth) x += (int) (speed/Math.sqrt(2));
            if (y - speed/Math.sqrt(2) - idle1.getHeight()/2 > 0) y -= (int) (speed/Math.sqrt(2));
        }
        else if (keyH.downPressed == true && keyH.leftPressed == true){
            direction = "left";
            if (x - speed/Math.sqrt(2) - idle1.getWidth()/2 > 0) x -= (int) (speed/Math.sqrt(2));
            if (y + speed/Math.sqrt(2) + idle1.getHeight()/2 < gp.screenHeight) y += (int) (speed/Math.sqrt(2));
        }
        else if (keyH.downPressed == true && keyH.rightPressed == true){
            direction = "right";
            if (x + speed/Math.sqrt(2) + idle1.getWidth()/2 < gp.screenWidth) x += (int) (speed/Math.sqrt(2));
            if (y + speed/Math.sqrt(2) + idle1.getHeight()/2 < gp.screenHeight) y += (int) (speed/Math.sqrt(2));
        }
        else if (keyH.upPressed == true){
            direction = "idle";
            if (y - speed - idle1.getHeight()/2 > 0) y -= speed;
        }
        else if (keyH.downPressed == true){
            direction = "idle";
            if (y + speed + idle1.getHeight()/2 < gp.screenHeight) y += speed;
        }
        else if (keyH.leftPressed == true){
            direction = "left";
            if (x - speed - idle1.getWidth()/2 > 0) x -= speed;
        }
        else if (keyH.rightPressed == true){
            direction = "right";
            if (x + speed + idle1.getWidth()/2 < gp.screenWidth) x += speed;
        }
        else direction = "idle";

        //focus
        if (keyH.focusPressed == true){
            focusing = true;
            speed = 3;
        }
        else {
            focusing = false;
            speed = 6;
        }

        //shoot
        if (keyH.shootPressed == true && cooldown == 0){
            if (focusing == true){
                Shooting focus = new Shooting(x, y, 90, 2);
                shoot.add(focus);
                Shooting amulet1 = new Shooting(x, y, 90, 1);
                shoot.add(amulet1);
                Shooting amulet2 = new Shooting(x, y, 92, 1);
                shoot.add(amulet2);
                Shooting amulet3 = new Shooting(x, y, 95, 1);
                shoot.add(amulet3);
                Shooting amulet4 = new Shooting(x, y, 88, 1);
                shoot.add(amulet4);
                Shooting amulet5 = new Shooting(x, y, 85, 1);
                shoot.add(amulet5);
            }
            else {
                Shooting amulet1 = new Shooting(x, y, 90, 1);
                shoot.add(amulet1);
                Shooting amulet2 = new Shooting(x, y, 100, 1);
                shoot.add(amulet2);
                Shooting amulet3 = new Shooting(x, y, 110, 1);
                shoot.add(amulet3);
                Shooting amulet4 = new Shooting(x, y, 80, 1);
                shoot.add(amulet4);
                Shooting amulet5 = new Shooting(x, y, 70, 1);
                shoot.add(amulet5);
                Shooting needle1 = new Shooting(x, y, 90, 3);
                shoot.add(needle1);
                Shooting needle2 = new Shooting(x - 15, y + 10, 90, 3);
                shoot.add(needle2);
                Shooting needle3 = new Shooting(x + 15, y + 10, 90, 3);
                shoot.add(needle3);
                Shooting needle4 = new Shooting(x - 30, y, 90, 3);
                shoot.add(needle4);
                Shooting needle5 = new Shooting(x + 30, y, 90, 3);
                shoot.add(needle5);
                Shooting needle6 = new Shooting(x - 45, y + 10, 90, 3);
                shoot.add(needle6);
                Shooting needle7 = new Shooting(x + 45, y + 10, 90, 3);
                shoot.add(needle7);
            }
            cooldown = 5;
        }
        else if (keyH.shootPressed == false) {
            cooldown = 5;
        }
        cooldown--;

        //sprite
        spriteCounter++;
        if (spriteCounter > 10){
            spriteNum = (spriteNum + 1) % 5;
            spriteCounter = 0;
        }

        //hitbox
        playerHitbox.update(x, y);
        for (int i = 0; i < hitboxesPool.size(); i++){
            if (invicible > 0) break;
            if (checkCollide(playerHitbox, hitboxesPool.get(i))){
                miss++;
                invicible = 5*60;
                gp.playSE(4);
                break;
                //System.out.println("Got hit:" + miss + " at " + playerHitbox.x + " " + playerHitbox.y);
            }
        }

        timer++;
    }

    public void draw(Graphics2D g2d){
        //shooting draw
        for (int i = 0; i < shoot.size(); i++){
            shoot.get(i).draw(g2d);
        }
        BufferedImage image = null;

        switch(direction){
            case "idle":
                if (spriteNum == 0){
                    image = idle1;
                }
                else if (spriteNum == 1){
                    image = idle2;
                }
                else if (spriteNum == 2){
                    image = idle3;
                }
                else if (spriteNum == 3){
                    image = idle4;
                }
                else if (spriteNum == 4){
                    image = idle5;
                }
                break;
            case "left":
                if (spriteNum == 0){
                    image = left1;
                }
                else if (spriteNum == 1){
                    image = left2;
                }
                else if (spriteNum == 2){
                    image = left3;
                }
                else if (spriteNum == 3){
                    image = left4;
                }
                else if (spriteNum == 4){
                    image = left5;
                }
                break;
            case "right":
                if (spriteNum == 0){
                    image = right1;
                }
                else if (spriteNum == 1){
                    image = right2;
                }
                else if (spriteNum == 2){
                    image = right3;
                }
                else if (spriteNum == 3){
                    image = right4;
                }
                else if (spriteNum == 4){
                    image = right5;
                }
                break;
        }

        if (invicible != 0){
            if (timer % 20 >= 10) {
                g2d.drawImage(image, (int) x - image.getWidth()/2, (int) y - image.getHeight()/2, image.getWidth(), image.getHeight(), null);
            }
        }
        else g2d.drawImage(image, (int) x - image.getWidth()/2, (int) y - image.getHeight()/2, image.getWidth(), image.getHeight(), null);

        //text
//        g2d.setColor(Color.WHITE);
//        g2d.setFont(new Font("Times New Roman", Font.PLAIN, 30));
//        g2d.drawString("Misscount: " + miss, 30, 30);

        //if (invicible > 0) g2d.drawString("Invicible: " + String.format("%.2f", (double)invicible/60), (int) x + 20, (int) y);


        //hitbox visible
//        g2d.setColor(Color.WHITE);
//        g2d.fillOval((int) x - 4, (int) y - 4, 8, 8);
//        g2d.setColor(Color.RED);
//        g2d.setStroke(new BasicStroke(2f));
//        g2d.drawOval((int) x - 4, (int) y - 4, 8, 8);

        //focus
        if (focusing == true) {
            AffineTransform at1 = AffineTransform.getTranslateInstance(x - hitbox.getWidth()/2, y - hitbox.getHeight()/2);
            at1.rotate(Math.toRadians(i-=0.05), hitbox.getWidth()/2, hitbox.getHeight()/2);
            g2d.drawImage(hitbox, at1, null);

            //yinyang balls
            AffineTransform at = AffineTransform.getTranslateInstance(x - 10 - yinyang.getWidth()/2, y - 40 - yinyang.getHeight()/2);
            at.rotate(Math.toRadians(i-=3), yinyang.getWidth()/2, yinyang.getHeight()/2);
            g2d.drawImage(yinyang, at, null);

            at = AffineTransform.getTranslateInstance(x + 10 - yinyang.getWidth()/2, y - 40 - yinyang.getHeight()/2);
            at.rotate(Math.toRadians(j+=3), yinyang.getWidth()/2, yinyang.getHeight()/2);
            g2d.drawImage(yinyang, at, null);
        }
        else {
            //yinyang balls
            AffineTransform at = AffineTransform.getTranslateInstance(x - 30 - yinyang.getWidth()/2, y - yinyang.getHeight()/2);
            at.rotate(Math.toRadians(i-=3), yinyang.getWidth()/2, yinyang.getHeight()/2);
            g2d.drawImage(yinyang, at, null);

            at = AffineTransform.getTranslateInstance(x + 30 - yinyang.getWidth()/2, y - yinyang.getHeight()/2);
            at.rotate(Math.toRadians(j+=3), yinyang.getWidth()/2, yinyang.getHeight()/2);
            g2d.drawImage(yinyang, at, null);
        }
    }
}
