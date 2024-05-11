package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

public class Enemy {
    GamePanel gp;
    double x, y, speed = 0.25, hitboxRange;
    BufferedImage idle1, ghost, fan;
    int spriteNum = 0, spriteCounter = 0;
    boolean fanVisible = false;
    public double HP, maxHP;

    public Enemy(GamePanel gp){
        this.gp = gp;
        this.x = gp.screenWidth/2;
        this.y = 150;
        setImg();
    }

    private void setImg(){
        try {
            idle1 = ImageIO.read(getClass().getResourceAsStream("/player/yuyuko_idle1.png"));
            ghost = ImageIO.read(getClass().getResourceAsStream("/player/yuyuko_ghost.png"));
            fan = ImageIO.read(getClass().getResourceAsStream("/player/yuyuko_fan.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setfanVisible(boolean b){
        fanVisible = b;
        if (b) hitboxRange = 300;
        else hitboxRange = 50;
    }

    public void setHP(double hp){
        maxHP = hp;
        HP = hp;
    }

    public void getHit(double damage){
        HP -= damage;
        HP = Math.max(HP, 0);
    }

    public void checkgetHit()
    {
        if (gp.section == gp.finalSpellSection) return;
        for (int i = 0; i < gp.player.shoot.size(); i++){
            if (gp.player.shoot.get(i) != null){
                Shooting s = gp.player.shoot.get(i);
                if (s.y >= this.y - 50 && s.y <= this.y + 50 && s.x >= this.x - hitboxRange
                        && s.x <= this.x + hitboxRange && !s.destroyed){
                    getHit(s.damage);
                    s.destroyed = true;
                    gp.playSE(3);
                }
            }
        }
    }

    public void update(){
        spriteCounter++;
        y += speed;
        if (spriteCounter > 40){
            spriteNum = (spriteNum + 1)%2;
            speed = -speed;
            spriteCounter = 0;
        }

        checkgetHit();
    }

    public void draw(Graphics2D g2d){
        //Sprite

        if (fanVisible) g2d.drawImage(fan, (int)x - fan.getWidth()/2, (int)y - fan.getHeight()/2, null);
        if (gp.section == gp.finalSpellSection) {
            g2d.drawImage(ghost, (int)x - ghost.getWidth()/2, (int)y - ghost.getHeight()/2, null);
            return;
        }
        else g2d.drawImage(idle1, (int) x - idle1.getWidth()/2, (int) y - idle1.getHeight()/2, null);

        //HP
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(4f));

        double HP_Angle = HP/maxHP * 360;
        g2d.drawArc((int) x - idle1.getWidth()/2 - 6, (int) y - idle1.getHeight()/2 - 6, idle1.getWidth() + 12, idle1.getHeight() + 12, 90, (int) HP_Angle);

        g2d.setStroke(new BasicStroke(1f));
        g2d.setColor(Color.RED);
        g2d.drawArc((int) x - idle1.getWidth()/2 - 8, (int) y - idle1.getHeight()/2 - 8, idle1.getWidth() + 16, idle1.getHeight() + 16, 0, 360);
        g2d.drawArc((int) x - idle1.getWidth()/2 - 4, (int) y - idle1.getHeight()/2 -4, idle1.getWidth() + 8, idle1.getHeight() + 8, 0, 360);

        //g2d.drawLine((int) (x - hitboxRange), (int) y, (int) (x + hitboxRange), (int) y);
        //g2d.setFont(new Font("Arial", Font.BOLD, 20));
        //g2d.drawString("HP: " + HP, (int) x + 50, (int) y);
    }
}
