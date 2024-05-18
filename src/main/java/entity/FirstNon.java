package entity;

import main.GamePanel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FirstNon {
    GamePanel gp;
    Font font;
    int timer = 0, time_out = 60*60;
    ArrayList<BulletSpawner> bullets = new ArrayList<>();

    public FirstNon(GamePanel gp){
        this.gp = gp;
        gp.yuyuko.setHP(25000);
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
            b.setImage("/sprite/purple_bullet.png");
            b.setHitbox(7);
            bullets.add(b);
        }
    }

    void genBubble(double x, double y, double speed){
        double deltaX = gp.player.x - x;
        double deltaY = gp.player.y - y;
        double angle = 10;
        double direction = Math.toDegrees(Math.atan(deltaY/deltaX));
        if (deltaX < 0) direction += 180;

        BulletSpawner b;
        for (double i = direction - 60; i <= direction + 60; i+=angle) {
            b = new BulletSpawner(x, y, speed, i);
            b.setImage("/sprite/purple_bubble.png");
            b.setHitbox(25);
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

    public void clearScreen() {
        bullets = new ArrayList();
    }

    private boolean checkEnd(){
        if (timer > time_out || gp.yuyuko.HP <= 0){
            gp.section++;
            gp.yuyuko.setHP(50000);
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

        //check hit
        gp.player.hitboxesPool = new ArrayList<>();
        for (BulletSpawner b : bullets) {
            gp.player.hitboxesPool.add(b.hitbox);
        }

        if (gp.player.invicible >= 4*60 + 30) {
            System.out.println("cleared");
            clearScreen();
        }
    }

    public void draw(Graphics2D g2d){
        if (checkEnd()) return;

        if (font == null){
            InputStream is = getClass().getResourceAsStream("/font/DCAi-W5-WIN-RKSJ-H-01.ttf");
            try {
                font = Font.createFont(font.TRUETYPE_FONT, is);
            } catch (FontFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(g2d);
        }

        g2d.setFont(font);
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40f));
        g2d.setColor(Color.PINK);

        int countdown = Math.max(0,(time_out - timer)) / 60;
        String timetxt = "" + countdown;
        if (countdown < 10) timetxt = "0" + countdown;
        g2d.drawString(timetxt, gp.screenWidth - 50, 50);
    }
}
