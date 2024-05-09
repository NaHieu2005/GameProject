package entity;

import main.GamePanel;
import java.awt.*;
import java.util.ArrayList;

public class FirstNon {
    GamePanel gp;
    int timer = 0, time_out = 60*60;
    ArrayList<BulletSpawner> bullets = new ArrayList<>();

    public FirstNon(GamePanel gp){
        this.gp = gp;
        gp.yuyuko.setHP(10);
    }

    public void genBullet(double x, double y, double speed){
        double deltaX = gp.player.x - x;
        double deltaY = gp.player.y - y;
        double angle = 30;
        double direction = Math.toDegrees(Math.atan(deltaY/deltaX));
        if (deltaX < 0) direction += 180;

        BulletSpawner b;
        for (double i = direction - angle; i <= direction + angle; i+=angle) {
            b = new BulletSpawner(x, y, speed, i);
            b.setImage("/player/purple_bullet.png");
            b.setHitbox(6);
            bullets.add(b);
        }
    }

    public void genBubble(double x, double y, double speed){
        double deltaX = gp.player.x - x;
        double deltaY = gp.player.y - y;
        double angle = 10;
        double direction = Math.toDegrees(Math.atan(deltaY/deltaX));
        if (deltaX < 0) direction += 180;

        BulletSpawner b;
        for (double i = direction - 60; i <= direction + 60; i+=angle) {
            b = new BulletSpawner(x, y, speed, i);
            b.setImage("/player/purple_bubble.png");
            b.setHitbox(24);
            bullets.add(b);
        }
    }

    void eraseBullet(){
        if (bullets.isEmpty()) return;

        while (bullets.getFirst().x < 0 || bullets.getFirst().y < 0
                    || bullets.getFirst().x > gp.screenWidth || bullets.getFirst().y > gp.screenHeight) {
                bullets.removeFirst();
        }
    }

    private boolean checkEnd(){
        if (timer > time_out || gp.yuyuko.HP <= 0){
            gp.section++;
            gp.yuyuko.setHP(10000);
            gp.stopMusic();
            gp.playMusic(7);
            return true;
        }
        return false;
    }

    public void update(){
        timer++;
        if (checkEnd()) return;
        if (timer < 3*60) return;
        eraseBullet();

        if (timer % 5 == 0){
            double x = Math.random() * gp.screenWidth;
            double y = Math.random() * 200;
            genBullet(x, y, 3);
        }
        if (timer % 60 == 0) {
            double x = Math.random() * gp.screenWidth;
            double y = Math.random() * 200;
            genBubble(x, y, 5);
        }
        for (BulletSpawner b : bullets) {
            b.update();
        }

        gp.player.hitboxesPool = new ArrayList<>();
        for (BulletSpawner b : bullets) {
            gp.player.hitboxesPool.add(b.hitbox);
        }

        //System.out.println(bullets.size());

        if (timer == time_out) gp.section++;
    }

    public void draw(Graphics2D g2d){
        if (checkEnd()) return;
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(g2d);
        }

        g2d.setFont(new Font("Arial", Font.PLAIN, 25));
        g2d.drawString("" + Math.max(0,(time_out - timer)) / 60, gp.screenWidth/2, 50);
    }
}
