package entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;

public class Shooting2 extends Shooting {

    public Shooting2(double x, double y, double angle, int type) {
        super(x, y, angle, type);
        switch (type) {
            case 1: //amulet
                speed = 15;
                damage = 20;
                hitbox = new Hitboxes(x, y, 10);
                break;
            case 2: //focus
                speed = 10;
                damage = 45;
                hitbox = new Hitboxes(x, y, 10);
                break;
            case 3:
                speed = 10;
                damage = 20;
                hitbox = new Hitboxes(x, y, 10);
        }
    }

    @Override
    public void getImg() {
        try {
            amulet = ImageIO.read(getClass().getResourceAsStream("/player/marisa_normal_shot.png"));
            focusShot = ImageIO.read(getClass().getResourceAsStream("/player/marisa_focus_shot.png"));
            needle = ImageIO.read(getClass().getResourceAsStream("/player/marisa_unfocus_shot.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D g2d){
        if (destroyed) return;
        switch (type) {
            case 1: //normal
                AffineTransform at = AffineTransform.getTranslateInstance(x - amulet.getWidth()/2,y - amulet.getHeight()/2);
                at.rotate(Math.toRadians(angle+90), amulet.getWidth()/2, amulet.getHeight()/2);

                g2d.drawImage(amulet, at, null);
                break;
            case 2: //focus
                g2d.drawImage(focusShot, (int)x - focusShot.getWidth()/2, (int)y - focusShot.getHeight()/2, null);
                break;
            case 3: //unfocus
                g2d.drawImage(needle, (int)x - needle.getWidth()/2, (int)y - needle.getHeight()/2, null);
                break;
        }

    }
}
