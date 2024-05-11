package entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

public class Shooting {
    public double x, y, speed, speedX, speedY, damage, angle;
    public int type, i;
    public boolean destroyed = false;
    public BufferedImage amulet, focusShot, needle;
    public Hitboxes hitbox;

    public Shooting(double x, double y, double angle, int type) {
        this.x = x;
        this.y = y;
        this.angle = angle + 180;
        this.type = type;
        switch (type) {
            case 1: //amulet
                speed = 15;
                damage = 10;
                hitbox = new Hitboxes(x, y, 10);
                break;
            case 2: //focus
                speed = 50;
                damage = 40;
                hitbox = new Hitboxes(x, y, 10);
                break;
            case 3:
                speed = 15;
                damage = 5;
                hitbox = new Hitboxes(x, y, 10);
        }
        getImg();
    }

    public void getImg(){
        try {
            amulet = ImageIO.read(getClass().getResourceAsStream("/player/reimu_amulet.png"));
            focusShot = ImageIO.read(getClass().getResourceAsStream("/player/reimu_focus_shot.png"));
            needle = ImageIO.read(getClass().getResourceAsStream("/player/needle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(){
        if (destroyed) return;
        i -= 5;
        speedX = speed * Math.cos(Math.toRadians(angle));
        speedY = speed * Math.sin(Math.toRadians(angle));
        x += speedX;
        y += speedY;
        if (hitbox != null) hitbox.update(x, y);
    }

    public void draw(Graphics2D g2d){
        if (destroyed) return;
        switch (type) {
            case 1: //amulet
                AffineTransform at = AffineTransform.getTranslateInstance(x - amulet.getWidth()/2,y - amulet.getHeight()/2);
                at.rotate(Math.toRadians(i), amulet.getWidth()/2, amulet.getHeight()/2);

                g2d.drawImage(amulet, at, null);
                break;
            case 2: //focus
                g2d.drawImage(focusShot, (int)x - focusShot.getWidth()/2, (int)y - focusShot.getHeight()/2, null);
                break;
            case 3:
                g2d.drawImage(needle, (int)x - needle.getWidth()/2, (int)y - needle.getHeight()/2, null);
                break;
        }
    }
}
