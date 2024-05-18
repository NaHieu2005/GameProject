package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Enemy {
    GamePanel gp;
    BufferedImage idle, right, left, ghost, fan;
    int spriteNum = 0, spriteCounter = 0;
    boolean fanVisible = false, isMoving = false;
    double x, y, speed = 0.25, angle;
    int toX, toY, hitboxRange;
    public double HP, maxHP;

    public Enemy(GamePanel gp){
        this.gp = gp;
        this.x = gp.screenWidth/2;
        this.y = 150;
        setImg();
    }

    private void setImg(){
        try {
            idle = ImageIO.read(getClass().getResourceAsStream("/sprite/yuyuko_idle.png"));
            right = ImageIO.read(getClass().getResourceAsStream("/sprite/yuyuko_right.png"));
            left = ImageIO.read(getClass().getResourceAsStream("/sprite/yuyuko_left.png"));
            ghost = ImageIO.read(getClass().getResourceAsStream("/sprite/yuyuko_ghost.png"));
            fan = ImageIO.read(getClass().getResourceAsStream("/sprite/yuyuko_fan.png"));
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

    private void getHit(double damage){
        HP -= damage;
        HP = Math.max(HP, 0);
        gp.damage_score += (int) damage * 1000;
    }

    private void checkgetHit()
    {
        if (gp.section == gp.finalSpellSection) return;
        if (gp.player == null) return;
        for (int i = 0; i < gp.player.shoot.size(); i++){
            if (gp.player.shoot.get(i) != null){
                Shooting s = gp.player.shoot.get(i);
                if (s.y >= this.y - 50 && s.y <= this.y + 50 && s.x >= this.x - 50
                        && s.x <= this.x + 50 && !s.destroyed){
                    getHit(s.damage * 1.5);
                    s.destroyed = true;
                    gp.playSE(3);
                }
                else if (s.y >= this.y - 50 && s.y <= this.y + 50 && s.x >= this.x - hitboxRange
                        && s.x <= this.x + hitboxRange && !s.destroyed){
                    getHit(s.damage);
                    s.destroyed = true;
                    gp.playSE(3);
                }
            }
        }
    }

    public void setMoveTo(int x, int y){
        this.toX = x;
        this.toY = y;
        this.isMoving = true;
    }

    private void move(){
        speed = 2;
        double deltaX = this.toX - x;
        double deltaY = this.toY - y;
        angle = Math.toDegrees(Math.atan(deltaY/deltaX));
        if (deltaX < 0) angle += 180;
        double speedX = speed * Math.cos(Math.toRadians(angle));
        double speedY = speed * Math.sin(Math.toRadians(angle));
        x += speedX;
        y += speedY;
        if (x >= toX - speed && x <= toX + speed && y >= toY - speed && y <= toY + speed){
            isMoving = false;
            speed = 0.25;
        }
    }

    public void update(){
        spriteCounter++;
        checkgetHit();
        if (isMoving) {
            move();
            return;
        }
        else if (speed != 0.25 && speed != -0.25) speed = 0.25;
        y += speed;
        if (spriteCounter > 40){
            spriteNum = (spriteNum + 1)%2;
            speed = -speed;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2d){
        //Sprite
        if (fanVisible) g2d.drawImage(fan, (int)x - fan.getWidth()/2, (int)y - fan.getHeight()/2, null);
        if (gp.section == gp.finalSpellSection) {
            g2d.drawImage(ghost, (int)x - ghost.getWidth()/2, (int)y - ghost.getHeight()/2, null);
            return;
        }
        else {
            if (isMoving){
                if (angle <= 90 && angle > -90){
                    g2d.drawImage(right, (int) x - idle.getWidth()/2, (int) y - idle.getHeight()/2, null);
                }
                else g2d.drawImage(left, (int) x - idle.getWidth()/2, (int) y - idle.getHeight()/2, null);
            }
            else g2d.drawImage(idle, (int) x - idle.getWidth()/2, (int) y - idle.getHeight()/2, null);
        }

        //HP
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(4f));

        double HP_Angle = HP/maxHP * 360;
        g2d.drawArc((int) x - idle.getWidth()/2 - 6, (int) y - idle.getHeight()/2 - 6, idle.getWidth() + 12, idle.getHeight() + 12, 90, (int) HP_Angle);

        g2d.setStroke(new BasicStroke(1f));
        g2d.setColor(Color.RED);
        g2d.drawArc((int) x - idle.getWidth()/2 - 8, (int) y - idle.getHeight()/2 - 8, idle.getWidth() + 16, idle.getHeight() + 16, 0, 360);
        g2d.drawArc((int) x - idle.getWidth()/2 - 4, (int) y - idle.getHeight()/2 -4, idle.getWidth() + 8, idle.getHeight() + 8, 0, 360);

        //hitbox visible
//        g2d.setColor(Color.WHITE);
//        g2d.drawRect((int) (x - hitboxRange), (int) y - 50, 2 * (int)hitboxRange, 100);
//        g2d.setColor(Color.RED);
//        g2d.drawRect((int) (x - 50), (int) y - 50, 100, 100);
    }
}
