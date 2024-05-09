package entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BulletSpawner{
    BufferedImage img;
    double x, y, speed, speedX, speedY, alpha, acceleration, angle_change, r;
    Hitboxes hitbox;

    public BulletSpawner(double x, double y, double speed, double alpha){
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.alpha = alpha;
    }

    public BulletSpawner(BulletSpawner another, double speed){
        this.img = another.img;
        this.x = another.x;
        this.y = another.y;
        this.r = another.r;
        this.alpha = another.alpha;
        this.hitbox = new Hitboxes(this.x, this.y, this.r);
        this.angle_change = another.angle_change;
        this.acceleration = another.acceleration;
        this.speed = speed;
    }

    public void setImage(String url){
        try {
            this.img = ImageIO.read(getClass().getResourceAsStream(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAngle_change(double angle_change){
        this.angle_change = angle_change;
    }

    public void setAcceleration(double acceleration){
        this.acceleration = acceleration;
    }

    public void setHitbox(double r){
        this.r = r;
        this.hitbox = new Hitboxes(x, y, r);
    }

    public void update() {
        alpha += angle_change;
        speedX = speed * Math.cos(Math.toRadians(alpha));
        speedY = speed * Math.sin(Math.toRadians(alpha));
        x += speedX;
        y += speedY;
        if (hitbox != null) hitbox.update(x, y);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(img, (int)x - img.getWidth(null)/2, (int) y - img.getHeight()/2, null);
        g2d.setColor(Color.RED);
        //g2d.fillOval((int)(x - r), (int)(y - r), (int)(2*r), (int)(2*r));
    }
}
