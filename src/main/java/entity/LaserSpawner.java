package entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class LaserSpawner {
    BufferedImage img;
    ArrayList<Hitboxes> hitboxes = new ArrayList<>();
    double x, y, angle, gapX, gapY;

    public LaserSpawner(double x, double y, double angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
        genHitbox();
    }

    public void setImage(String url){
        try {
            this.img = ImageIO.read(getClass().getResourceAsStream(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void genHitbox(){
        gapX = 20*Math.cos(Math.toRadians(angle));
        gapY = 20*Math.sin(Math.toRadians(angle));
        double X = x;
        double Y = y;
        int i = 30;
        while (i > 0){
            i--;
            X += gapX;
            Y += gapY;
            Hitboxes h = new Hitboxes(X, Y, 5);
            hitboxes.add(h);
        }
    }

    public ArrayList<Hitboxes> getHitbox(){
        return hitboxes;
    }

    public void draw(Graphics2D g2d){
        AffineTransform at = AffineTransform.getTranslateInstance(x + gapX*20 - img.getWidth()/2,y + gapY*20 - img.getHeight()/2);
        at.rotate(Math.toRadians(angle+90), img.getWidth()/2, img.getHeight()/2);
        g2d.drawImage(img, at, null);
        g2d.setColor(Color.RED);
//        for (int i = 0; i < hitboxes.size(); i++){
//            Hitboxes h = hitboxes.get(i);
//            g2d.fillOval((int)(h.x - h.r), (int)(h.y - h.r), (int)(2*h.r), (int)(2*h.r));
//        }
    }
}
