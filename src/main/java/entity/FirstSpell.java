package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FirstSpell {
    GamePanel gp;
    Enemy enemy;
    Font font;
    BufferedImage spell_circle;
    int timer = 0, time_out = 98*60;
    int bonus_score = 3000000;
    int capture_bonus = 5000000;
    boolean missed = false;
    int i = 0;
    ArrayList<BulletSpawner> butterflies = new ArrayList<>();
    ArrayList<BulletSpawner> rices = new ArrayList<>();
    ArrayList<BulletSpawner> bubble = new ArrayList<>();

    public FirstSpell(GamePanel gp){
        this.gp = gp;
        this.enemy = gp.yuyuko;
        InputStream is = getClass().getResourceAsStream("/font/DCAi-W5-WIN-RKSJ-H-01.ttf");
        try {
            font = Font.createFont(font.TRUETYPE_FONT, is);
            spell_circle = ImageIO.read(getClass().getResourceAsStream("/sprite/circle.png"));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void spawnButterfly(){
        double rng = Math.random();
        String bullet_type;
        if (timer%360 == 0) bullet_type = "/sprite/blue_butterfly.png";
        else bullet_type = "/sprite/purple_butterfly.png";

        BulletSpawner b = new BulletSpawner(enemy.x, enemy.y, 2, 15 + rng*60);
        b.setImage(bullet_type);
        b.setAcceleration(-0.15);
        b.setHitbox(5);
        butterflies.add(b);
        for (int i = 0; i < 6; i++) {
            b = new BulletSpawner(b, b.speed + 3);
            b.setAcceleration(b.acceleration - 0.05);
            butterflies.add(b);
        }

        b = new BulletSpawner(enemy.x, enemy.y, 2, 105 + rng*60);
        b.setAcceleration(-0.15);
        b.setHitbox(5);
        b.setImage(bullet_type);
        butterflies.add(b);
        for (int i = 0; i < 6; i++) {
            b = new BulletSpawner(b, b.speed + 3);
            b.setAcceleration(b.acceleration - 0.05);
            butterflies.add(b);
        }
    }

    private void spawnRice(){
        double rng = Math.random();
        double angle = Math.random();
        double speed_rng = Math.random();
        if (angle > 0.5) angle = 90;
        else angle = -90;
        BulletSpawner b = new BulletSpawner(gp.screenWidth * rng ,enemy.y - 10 + rng*20, 1 + speed_rng, angle);
        b.setHitbox(4);
        b.setImage("/sprite/rice.png");
        rices.add(b);
    }

    public void spawnBubble(int begin, double angle){
        ArrayList<BulletSpawner> temp = new ArrayList<>();
        BulletSpawner b = new BulletSpawner(enemy.x, enemy.y, 3, 5);
        b.setHitbox(25);
        for (int i = begin; i <= 360; i += angle){
            b = new BulletSpawner(b, b.speed);
            b.setAngle(i);
            b.setImage("/sprite/purple_bubble.png");
            bubble.add(b);
            for (int j = 1; j <= 4; j++){
                BulletSpawner c = new BulletSpawner(b, b.speed);
                c.setAcceleration(0.01*j);
                temp.add(c);
            }
        }
        bubble.addAll(temp);
    }

    private void butterflyManager(){
        ArrayList<BulletSpawner> temp = new ArrayList<>();

        for (int i = 0; i < butterflies.size(); i++) {
            //Spin
            if (butterflies.get(i).getTimer() == 45){
                butterflies.get(i).setAcceleration(0.005);
                if (butterflies.get(i).alpha < 90) {
                    butterflies.get(i).setAngle_change(1);
                }
                else butterflies.get(i).setAngle_change(-1);
                for (int j = 1; j <= 2; j++){
                    BulletSpawner b = new BulletSpawner(butterflies.get(i), butterflies.get(i).speed);
                    b.setAngle(butterflies.get(i).alpha + 120*j);
                    b.timer = 10000*j;
                    temp.add(b);
                }
            }
            //Aim
            double deltaX = gp.player.x - butterflies.get(i).x;
            double deltaY = gp.player.y - butterflies.get(i).y;
            double direction = Math.toDegrees(Math.atan(deltaY/deltaX));
            if (deltaX < 0) direction += 180;
            if (butterflies.get(i).getTimer() == 225){
                butterflies.get(i).setAngle(direction);
                butterflies.get(i).setAngle_change(0);
                for (double j = 0; j < 4; j++){
                    BulletSpawner b = new BulletSpawner(butterflies.get(i), butterflies.get(i).speed + 1*j);
                    b.timer = 226;
                    temp.add(b);
                }
            }

            if (butterflies.get(i).getTimer() == 10180){
                if (deltaX < 0) direction += 180;
                butterflies.get(i).setAngle(direction+120);
                butterflies.get(i).setAngle_change(0);
                for (double j = 0; j < 4; j++){
                    BulletSpawner b = new BulletSpawner(butterflies.get(i), butterflies.get(i).speed + 1*j);
                    b.timer = 226;
                    temp.add(b);
                }
            }

            if (butterflies.get(i).getTimer() == 20180){
                if (deltaX < 0) direction += 180;
                butterflies.get(i).setAngle(direction-120);
                butterflies.get(i).setAngle_change(0);
                for (double j = 0; j < 4; j++){
                    BulletSpawner b = new BulletSpawner(butterflies.get(i), butterflies.get(i).speed + 1*j);
                    b.timer = 226;
                    temp.add(b);
                }
            }
        }
        butterflies.addAll(temp);
    }

    private void eraseBullet(ArrayList<BulletSpawner> temp){
        if (!temp.isEmpty()){
            while (temp.getFirst().x <= 0 || temp.getFirst().y <= 0
                    || temp.getFirst().x >= gp.screenWidth || temp.getFirst().y >= gp.screenHeight) {
                temp.removeFirst();
            }
        }
    }

    public void clearScreen() {
        butterflies = new ArrayList();
        rices = new ArrayList();
        bubble = new ArrayList();
    }

    private boolean checkEnd(){
        if (timer > time_out || gp.yuyuko.HP <= 0){
            gp.section++;
            gp.yuyuko.setHP(0);
            gp.yuyuko.x = gp.screenWidth/2;
            gp.yuyuko.y = 150;
            gp.yuyuko.isMoving = false;
            gp.stopMusic();
            gp.playMusic(7);
            gp.spellBonus_score += bonus_score - bonus_score * timer / time_out;
            if (!missed) gp.spellBonus_score += capture_bonus - capture_bonus * timer / time_out;
            return true;
        }
        return false;
    }

    public void update(){
        timer++;
        i++;
        //erase bullet;
        eraseBullet(butterflies);
        eraseBullet(rices);

        //check hit
        if (gp.player != null){
            gp.player.hitboxesPool = new ArrayList<>();
            if (gp.player.invicible >= 4*60 + 30) {
                System.out.println("cleared");
                clearScreen();
            }

            if (gp.player.invicible == 5*60) {
                missed = true;
            }
        }

        //check end
        if (checkEnd()) return;

        //spawn bullets
        if (timer == 180) spawnBubble(5, 9);
        if (timer > 300 && timer%600 == 300) spawnBubble(0, 18);
        if (timer > 180 && timer%180 == 0) spawnButterfly();
        if (timer%3 == 0 && timer > 180) spawnRice();
        butterflyManager();

        //boss move
        if (timer % 360 == 0 && timer > 0) enemy.setMoveTo((int) (Math.random()*400 + 100), (int) (50 + Math.random()*200));

        //bullet update
        for (int i = 0; i < butterflies.size(); i++) {
            butterflies.get(i).update();
            gp.player.hitboxesPool.add(butterflies.get(i).hitbox);
        }

        for (int i = 0; i <rices.size(); i++){
            rices.get(i).update();
            gp.player.hitboxesPool.add(rices.get(i).hitbox);
        }

        for (int i = 0; i <bubble.size(); i++){
            bubble.get(i).update();
            gp.player.hitboxesPool.add(bubble.get(i).hitbox);
        }
    }

    public void draw(Graphics2D g2d){
        if (checkEnd()) return;
        AffineTransform at = AffineTransform.getTranslateInstance(enemy.x - 0.8*spell_circle.getWidth()/2,enemy.y - 0.8*spell_circle.getHeight()/2);
        at.rotate(Math.toRadians(i), 0.8*spell_circle.getWidth()/2, 0.8*spell_circle.getHeight()/2);
        at.scale(0.8, 0.8);
        g2d.drawImage(spell_circle, at, null);

        for (int i = 0; i < butterflies.size(); i++) {
            butterflies.get(i).draw(g2d);
        }

        for (int i = 0; i < rices.size(); i++){
            rices.get(i).draw(g2d);
        }

        for (int i = 0; i < bubble.size(); i++){
            bubble.get(i).draw(g2d);
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
