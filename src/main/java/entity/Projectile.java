package entity;

import main.GamePanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;

import static main.GamePanel.numberOfBullets;

public class Projectile{
    GamePanel gp;
    BulletManager bulletManager;
    public int timer = 1;
    public int cycle = 9*60;
    BufferedImage bg, spell_circle;
    int time_out = 67*60, i = 0;
    double x, y, scale = 1;

    ArrayList<BulletSpawner> bubble = new ArrayList<>();
    ArrayList<BulletSpawner>[] blue_butterfly = new ArrayList[3];
    ArrayList<BulletSpawner>[] red_butterfly = new ArrayList[8];


    public Projectile(GamePanel gp) {
        this.gp = gp;
        this.x = gp.screenWidth/2;
        this.y = 150;
        this.bulletManager = new BulletManager(x, y, 0);
        getImg();
    }

    void getImg(){
        try {
            spell_circle = ImageIO.read(getClass().getResourceAsStream("/player/circle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void red_wave(int cnt){
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


    public void blue_wave(int idx){
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

    private ArrayList<BulletSpawner> copy(ArrayList<BulletSpawner> list){
        ArrayList<BulletSpawner> temp = new ArrayList<>();
        for (BulletSpawner b : list){
            temp.add(new BulletSpawner(b, b.speed));
        }
        return temp;
    }

    public void update(){
        ArrayList<BulletSpawner> temp = new ArrayList<>();

        scale-=0.0002; i+=2;
        timer++;
        gp.player.hitboxesPool = new ArrayList<>();

        if (timer == time_out + 75) gp.FPS = 20;
        if (timer >= time_out + 100){
            clearScreen();
            if (timer == time_out + 160) {
                gp.FPS = 60;
                gp.section++;
            }
        }

        if (timer % cycle == 0) {
            bulletManager = new BulletManager(x, y, timer/cycle);
        }

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
            //System.out.println(bubble.get(i).hitbox.x + " " + bubble.get(i).hitbox.y);
            bubble.get(i).update();
            gp.player.hitboxesPool.add(bubble.get(i).hitbox);
        }
        numberOfBullets += bubble.size();

        //miss state
        if (gp.player.invicible >= 4*60 + 30) {
            System.out.println("cleared");
            clearScreen();
        }

        //System.out.println(gp.player.hitboxesPool.size());
        //System.out.println("timer: " + (double)timer/60);

    }


    public void draw(Graphics2D g2d) {
        AffineTransform at = AffineTransform.getTranslateInstance(x - scale*spell_circle.getWidth()/2,y - scale*spell_circle.getHeight()/2);
        at.rotate(Math.toRadians(i), scale * spell_circle.getWidth()/2, scale * spell_circle.getHeight()/2);
        at.scale(scale, scale);
        g2d.drawImage(spell_circle, at, null);

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

        g2d.setFont(new Font("Arial", Font.PLAIN, 25));
        g2d.drawString("" + Math.max(0,(time_out - timer)) / 60, gp.screenWidth/2, 50);
    }

    public void clearScreen() {
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

