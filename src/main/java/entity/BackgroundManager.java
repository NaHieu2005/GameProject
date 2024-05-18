package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class BackgroundManager {
    GamePanel gp;
    BufferedImage bg, spell_bg, tree, bg_circle;
    int timer = 0, i = 0;

    public BackgroundManager(GamePanel gp){
        this.gp = gp;
        getImg();
    }

    public class Cherry{
        BufferedImage img;
        double x, y, speed;

        public Cherry(double speed, int type){
            this.speed = speed;
            try {
                if (type == 0) img = ImageIO.read(getClass().getResourceAsStream("/sprite/cherry0.png"));
                if (type == 1) img = ImageIO.read(getClass().getResourceAsStream("/sprite/cherry1.png"));
                if (type == 2) img = ImageIO.read(getClass().getResourceAsStream("/sprite/cherry2.png"));

            } catch (IOException e) {
                e.printStackTrace();
            }
            this.x = 0;
            this.y = gp.screenHeight;
        }

        public void setPos(double x, double y){
            this.x = x;
            this.y = y;
        }

        public void update(){
            y -= speed;
        }

        public void draw(Graphics2D g2d){
            g2d.drawImage(img,(int)x,(int)y, gp.screenWidth, gp.screenWidth/2,null);
        }
    }

    private void getImg(){
        try {
            bg = ImageIO.read(getClass().getResourceAsStream("/sprite/s6_bg.png"));
            spell_bg = ImageIO.read(getClass().getResourceAsStream("/sprite/spellcard_bg.png"));
            tree = ImageIO.read(getClass().getResourceAsStream("/sprite/spellcard_tree.png"));
            bg_circle = ImageIO.read(getClass().getResourceAsStream("/sprite/bg_circle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void erase(){
        if (!cherries0.isEmpty()) {
            while (cherries0.getFirst().y < -500) {
                cherries0.removeFirst();
            }
        }

        if (!cherries1.isEmpty()) {
            while (cherries1.getFirst().y < -500) {
                cherries1.removeFirst();
            }
        }

        if (!cherries2.isEmpty()) {
            while (cherries2.getFirst().y < -500) {
                cherries2.removeFirst();
            }
        }
    }

    ArrayList<Cherry> cherries0 = new ArrayList<>();
    ArrayList<Cherry> cherries1 = new ArrayList<>();
    ArrayList<Cherry> cherries2 = new ArrayList<>();
    ArrayList<BGEffect> effects = new ArrayList<>();
    public void update(){
        timer++;
        i++;
        erase();

        //init
        if (cherries0.isEmpty()){
            for (int i = 5; i >= 0; i--) {
                Cherry cherry = new Cherry(1.6, 0);
                cherry.setPos(0, gp.screenHeight - i * gp.screenWidth/2);
                cherries0.add(cherry);
            }
        }

        if (cherries1.isEmpty()){
            for (int i = 5; i >= 0; i--) {
                Cherry cherry = new Cherry(2, 1);
                cherry.setPos(0, gp.screenHeight - i * gp.screenWidth/2);
                cherries1.add(cherry);
            }
        }

        if (cherries2.isEmpty()){
            for (int i = 5; i >= 0; i--) {
                Cherry cherry = new Cherry(2.5, 2);
                cherry.setPos(0, gp.screenHeight - i * gp.screenWidth/2);
                cherries2.add(cherry);
            }
        }

        if (effects.isEmpty()){
            BGEffect effect = new BGEffect(gp);
            effect.setPos(0,0);
            effects.add(effect);
        }

        //loop
        if (Math.round(cherries0.getLast().y) == gp.screenHeight - gp.screenWidth/2){
            Cherry cherry = new Cherry(1.6, 0);
            cherries0.add(cherry);
        }

        if (cherries1.getLast().y == gp.screenHeight - gp.screenWidth/2){
            Cherry cherry = new Cherry(2, 1);
            cherries1.add(cherry);
        }

        if (cherries2.getLast().y == gp.screenHeight - gp.screenWidth/2){
            Cherry cherry = new Cherry(2.5, 2);
            cherries2.add(cherry);
        }

        if (effects.getLast().y == 0){
            BGEffect effect = new BGEffect(gp);
            effects.add(effect);
        }

        //update
        for (int i = 0; i < cherries0.size(); i++){
            cherries0.get(i).update();
        }

        for (int i = 0; i < cherries1.size(); i++){
            cherries1.get(i).update();
        }

        for (int i = 0; i < cherries2.size(); i++){
            cherries2.get(i).update();
        }

        for (int i = 0; i < effects.size(); i++){
            effects.get(i).update();
        }
    }

    public void draw(Graphics2D g2d){
        if (gp.section == gp.finalSpellSection || gp.section == gp.firstSpellSection) {
            for (int i = 0; i < effects.size(); i++){
                effects.get(i).draw(g2d);
            }
            g2d.drawImage(tree,0,0, gp.screenWidth, gp.screenHeight, null);
        }
        else{
            g2d.drawImage(bg,0,0, gp.screenWidth, gp.screenHeight, null);

            AffineTransform at = AffineTransform.getTranslateInstance(gp.screenWidth/2 - bg_circle.getWidth()/2,200 - bg_circle.getHeight()/2);
            at.rotate(Math.toRadians(i), bg_circle.getWidth()/2, bg_circle.getHeight()/2);
            g2d.drawImage(bg_circle, at, null);

            for (int i = 0; i < cherries0.size(); i++){
                cherries0.get(i).draw(g2d);
            }

            for (int i = 0; i < cherries1.size(); i++){
                cherries1.get(i).draw(g2d);
            }

            for (int i = 0; i < cherries2.size(); i++){
                cherries2.get(i).draw(g2d);
            }
        }
    }
}
