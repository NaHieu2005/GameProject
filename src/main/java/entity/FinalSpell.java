package entity;

import main.GamePanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static main.GamePanel.numberOfBullets;

public class FinalSpell {
    GamePanel gp;
    Enemy enemy;
    BulletManager bulletManager;
    BufferedImage spell_circle, effect1, effect2;
    Font font;
    public int timer = 1;
    public int cycle = 9*60;
    int time_out = 67*60, i = 0;
    int bonus_score = 3000000;
    int capture_bonus = 5000000;
    double x, y, scale = 1;
    double laser_gap;
    boolean missed = false;

    ArrayList<BulletSpawner> bubble = new ArrayList<>();
    ArrayList<BulletSpawner>[] blue_butterfly = new ArrayList[3];
    ArrayList<BulletSpawner>[] red_butterfly = new ArrayList[8];
    ArrayList<LaserSpawner> lasers = new ArrayList<>();


    public FinalSpell(GamePanel gp) {
        this.gp = gp;
        this.enemy = gp.yuyuko;
        this.x = enemy.x;
        this.y = enemy.y;
        this.bulletManager = new BulletManager(x, y, 0);
        getImg();
    }

    private void getImg(){
        try {
            spell_circle = ImageIO.read(getClass().getResourceAsStream("/sprite/circle.png"));
            effect1 = ImageIO.read(getClass().getResourceAsStream("/sprite/effect1.png"));
            effect2 = ImageIO.read(getClass().getResourceAsStream("/sprite/effect2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void red_wave(int cnt){
        ArrayList<BulletSpawner> temp = new ArrayList<>();

        //1
        if (timer % cycle == 5*60 + cnt*15) {
            if (red_butterfly[cnt] == null) red_butterfly[cnt] = new ArrayList<>();
            else red_butterfly[cnt].clear();
            if (cnt%4 < 2) red_butterfly[cnt].addAll(copy(bulletManager.red_butterfly_1));
            else red_butterfly[cnt].addAll(copy(bulletManager.red_butterfly_3));
        }

        if (timer % cycle == 5*60 + 120 + cnt*15) {
            for (BulletSpawner b : red_butterfly[cnt]) {
                temp.add(new BulletSpawner(b, b.speed + 2));
                temp.add(new BulletSpawner(b, b.speed + 4));
            }
            red_butterfly[cnt].addAll(temp);
        }

        //2
        if (timer % cycle == 5*60  + (cnt+1)*15){
            if (red_butterfly[cnt+1] == null) red_butterfly[cnt+1] = new ArrayList<>();
            else red_butterfly[cnt+1].clear();
            if (cnt%4 < 2) red_butterfly[cnt+1].addAll(copy(bulletManager.red_butterfly_2));
            else red_butterfly[cnt+1].addAll(copy(bulletManager.red_butterfly_4));
        }

        if (timer % cycle == 5*60 + 120 + (cnt+1)*15){
            for (BulletSpawner b : red_butterfly[cnt+1]){
                temp.add(new BulletSpawner(b, b.speed + 2));
                temp.add(new BulletSpawner(b, b.speed + 4));
            }
            red_butterfly[cnt+1].addAll(temp);
        }
    }


    private void blue_wave(int idx){
        ArrayList<BulletSpawner> temp = new ArrayList<>();

        if (timer % cycle == 3*60 + 20*idx){
            if (blue_butterfly[idx] == null) blue_butterfly[idx] = new ArrayList<>();
            else blue_butterfly[idx].clear();
            blue_butterfly[idx].addAll(copy(bulletManager.blue_butterfly));
        }

        if (timer % cycle == 3*60 + 10 + 20*idx){
            blue_butterfly[idx].addAll(copy(bulletManager.purple_butterfly));
        }

        if (timer % cycle == 3*60 + 120 + 20*idx){
            for (int i = 0; i < blue_butterfly[idx].size(); i++){
                BulletSpawner current = blue_butterfly[idx].get(i);
                temp.add(new BulletSpawner(current, current.speed + 2));
                temp.add(new BulletSpawner(current, current.speed + 4));
            }
            blue_butterfly[idx].addAll(temp);
        }
    }

    void laserManager(){
        if (timer % cycle == 240){
            int wave = timer/cycle;
            laser_gap = 360/(3 + (double)wave);
            lasers = new ArrayList<>();
            for (double angle = 90 + laser_gap/4; angle < 360 + 90 + laser_gap/4; angle += laser_gap){
                double radius = 100;
                double X = x + radius*Math.cos(Math.toRadians(angle));
                double Y = y + radius*Math.sin(Math.toRadians(angle));
                LaserSpawner laser = new LaserSpawner(X, Y, angle);
                laser.setImage("/sprite/purple_laser.png");
                lasers.add(laser);
            }

            for (double angle = 90 + 3*laser_gap/4; angle < 360 + 90 + 3*laser_gap/4; angle += laser_gap){
                double radius = 100;
                double X = x + radius*Math.cos(Math.toRadians(angle));
                double Y = y + radius*Math.sin(Math.toRadians(angle));
                LaserSpawner laser = new LaserSpawner(X, Y, angle);
                laser.setImage("/sprite/blue_laser.png");
                lasers.add(laser);
            }
        }
        if (timer % cycle >= 240 && timer % cycle <= 420){
            for (int i = 0; i < lasers.size(); i++){
                gp.player.hitboxesPool.addAll(lasers.get(i).getHitbox());
            }
        }
    }

    private ArrayList<BulletSpawner> copy(ArrayList<BulletSpawner> list){
        ArrayList<BulletSpawner> temp = list.stream().map(b -> new BulletSpawner(b, b.speed)).collect(Collectors.toCollection(ArrayList::new));
        return temp;
    }

    boolean checkEnd(){
        if (timer == time_out + 75) gp.FPS = 20;
        if (timer >= time_out + 100){
            clearScreen();
            if (timer == time_out + 160) {
                gp.FPS = 60;
                gp.spellBonus_score += bonus_score;
                if (!missed) gp.spellBonus_score += capture_bonus;
                gp.player.hitboxesPool = new ArrayList<>();
                gp.section++;
                return true;
            }
        }
        return false;
    }

    public void update(){
        ArrayList<BulletSpawner> temp = new ArrayList<>();

        scale-=0.0002; i+=2;
        timer++;
        gp.player.hitboxesPool = new ArrayList<>();

        //end
        if (checkEnd()) return;

        //move
        enemy.x -=0.02;
        x -= 0.02;

        //wave buff
        if (timer % cycle == 0) {
            bulletManager = new BulletManager(x, y, timer/cycle);
        }

        //laser
        laserManager();

        //blue wave
        for (int idx = 0; idx < 3; idx++){
            if (timer <= time_out + 2*60) blue_wave(idx);
            if (blue_butterfly[idx] != null) {
                numberOfBullets += blue_butterfly[idx].size();
                for (int i = 0; i < blue_butterfly[idx].size(); i++) {
                    blue_butterfly[idx].get(i).update();
                    gp.player.hitboxesPool.add(blue_butterfly[idx].get(i).hitbox);
                }
            }
        }

        //red wave
        for (int idx = 0; idx < 8; idx+=2) {
            if (timer < time_out) red_wave(idx);
            if (red_butterfly[idx] != null) {
                numberOfBullets += red_butterfly[idx].size();
                for (int i = 0; i < red_butterfly[idx].size(); i++) {
                    red_butterfly[idx].get(i).update();
                    gp.player.hitboxesPool.add(red_butterfly[idx].get(i).hitbox);
                }
            }
            if (red_butterfly[idx+1] != null) {
                numberOfBullets += red_butterfly[idx+1].size();
                for (int i = 0; i < red_butterfly[idx+1].size(); i++) {
                    red_butterfly[idx+1].get(i).update();
                    gp.player.hitboxesPool.add(red_butterfly[idx+1].get(i).hitbox);
                }
            }
        }

        //bubble wave
        if (timer < time_out && timer % cycle == 7*60){
            if (bubble == null) bubble = new ArrayList<>();
            bubble.clear();
            bubble.addAll(copy(bulletManager.bubble));
        }

        if (timer % cycle == 8*60 + 59){
            for (int i = 0; i < bubble.size(); i++){
                BulletSpawner b = bubble.get(i);
                temp.add(new BulletSpawner(b, b.speed + 2));
                temp.add(new BulletSpawner(b, b.speed + 4));
            }
            bubble.addAll(temp);
            temp.clear();
        }

        for (int i = 0 ; i < bubble.size(); i++){
            bubble.get(i).update();
            gp.player.hitboxesPool.add(bubble.get(i).hitbox);
        }
        numberOfBullets += bubble.size();

        //check miss
        if (gp.player.invicible >= 4*60 + 30) {
            System.out.println("cleared");
            clearScreen();
        }

        if (gp.player.invicible == 5*60) {
            missed = true;
        }
    }


    public void draw(Graphics2D g2d) {
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

        //Circle
        AffineTransform at = AffineTransform.getTranslateInstance(x - scale*spell_circle.getWidth()/2,y - scale*spell_circle.getHeight()/2);
        at.rotate(Math.toRadians(i), scale * spell_circle.getWidth()/2, scale * spell_circle.getHeight()/2);
        at.scale(scale, scale);
        g2d.drawImage(spell_circle, at, null);

        //bullets
        if (timer % cycle >= 240 && timer % cycle <= 420){
            for (int i = 0; i < lasers.size(); i++){
                LaserSpawner l = lasers.get(i);
                l.draw(g2d);
                if (i < lasers.size()/2) g2d.drawImage(effect1, (int)l.x - effect1.getWidth()/2, (int)l.y - effect1.getHeight()/2, null);
                else g2d.drawImage(effect2, (int)l.x - effect2.getWidth()/2, (int)l.y - effect2.getHeight()/2, null);
            }
        }
        else if (timer % cycle >= 180 && timer % cycle <= 240){
            int wave = timer/cycle;
            laser_gap = 360/(3 + (double)wave);
            g2d.setColor(Color.MAGENTA);
            double begin = (double) (timer % cycle - 180) / 60 * laser_gap + laser_gap/4 + 90;
            for (double angle = begin; angle < 360 + begin; angle += laser_gap){
                double radius = 100;
                double X1 = x + radius*Math.cos(Math.toRadians(angle));
                double Y1 = y + radius*Math.sin(Math.toRadians(angle));
                radius = 1000;
                double X2 = x + radius*Math.cos(Math.toRadians(angle));
                double Y2 = y + radius*Math.sin(Math.toRadians(angle));
                g2d.drawLine((int) X1, (int)Y1, (int)X2, (int)Y2);
                g2d.drawImage(effect1, (int)X1 - effect1.getWidth()/2, (int)Y1 - effect1.getHeight()/2, null);
            }
            g2d.setColor(Color.CYAN);
            begin = (double) (180 - timer % cycle) / 60 * laser_gap + 3*laser_gap/4 + 90;
            for (double angle = begin; angle < 360 + begin; angle += laser_gap){
                double radius = 100;
                double X1 = x + radius*Math.cos(Math.toRadians(angle));
                double Y1 = y + radius*Math.sin(Math.toRadians(angle));
                radius = 1000;
                double X2 = x + radius*Math.cos(Math.toRadians(angle));
                double Y2 = y + radius*Math.sin(Math.toRadians(angle));
                g2d.drawLine((int) X1, (int)Y1, (int)X2, (int)Y2);
                g2d.drawImage(effect2, (int)X1 - effect2.getWidth()/2, (int)Y1 - effect2.getHeight()/2, null);
            }
        }


        for (int i = 0; i < bubble.size(); i++){
            bubble.get(i).draw(g2d);
        }

        for (int i = 0; i < 3; i++) {
            if (blue_butterfly[i] != null)
                for (int j = 0; j < blue_butterfly[i].size(); j++){
                    blue_butterfly[i].get(j).draw(g2d);
                }
        }
        for (int i = 0; i < 8; i++){
            if (red_butterfly[i] != null)
                for (int j = 0; j < red_butterfly[i].size(); j++){
                    red_butterfly[i].get(j).draw(g2d);
                }
        }

        //countdown
        g2d.setFont(font);
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40f));
        g2d.setColor(Color.PINK);

        int countdown = Math.max(0,(time_out - timer)) / 60;
        String timetxt = "" + countdown;
        if (countdown < 10) timetxt = "0" + countdown;
        g2d.drawString(timetxt, gp.screenWidth - 50, 50);;
    }

    private void clearScreen() {
        for (int i = 0; i < 3; i++) {
            if (blue_butterfly[i] != null){
                blue_butterfly[i] = new ArrayList<>();
            }
        }

        for (int i = 0; i < 8; i++) {
            if (red_butterfly[i] != null){
                red_butterfly[i] = new ArrayList<>();
            }
        }

        if (bubble != null) bubble =  new ArrayList<>();
    }
}

