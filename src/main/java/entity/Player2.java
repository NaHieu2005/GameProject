package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.io.IOException;

import static entity.Hitboxes.checkCollide;

public class Player2 extends Player{
    public Player2(GamePanel gp, KeyHandler keyH) {
        super(gp, keyH);
        playerHitbox = new Hitboxes(x , y, 6);
        speed = 8;
    }

    @Override
    public void getPlayerImage() {
        try {
            idle1 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_idle1.png"));
            idle2 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_idle2.png"));
            idle3 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_idle3.png"));
            idle4 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_idle4.png"));
            idle5 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_idle5.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_left2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_left3.png"));
            left4 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_left4.png"));
            left5 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_left5.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_right2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_right3.png"));
            right4 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_right4.png"));
            right5 = ImageIO.read(getClass().getResourceAsStream("/player/marisa_right5.png"));
            yinyang = ImageIO.read(getClass().getResourceAsStream("/player/magic.png"));
            hitbox = ImageIO.read(getClass().getResourceAsStream("/player/player_hitbox.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        i+=3; j-=3; k-=3;
        for (int i = 0; i < shoot.size(); i++){
            Shooting s = shoot.get(i);
            if (s.destroyed == true || s.x < 0 || s.x > gp.screenWidth || s.y < 0){
                shoot.remove(i);
            }
            else shoot.get(i).update();
        }

        if (invicible > 0) invicible--;
        if (invicible > 4.5*60) return;
        if (invicible == 4.5*60){
            x = gp.screenWidth/2;
            y = 700;
        }
        //move
        if (keyH.upPressed == true && keyH.leftPressed == true){
            direction = "left";
            if (x - speed/Math.sqrt(2) - idle1.getWidth()/2 > 0) x -= (int) (speed/Math.sqrt(2));
            if (y - speed/Math.sqrt(2) - idle1.getHeight()/2 > 0) y -= (int) (speed/Math.sqrt(2));
        }
        else if (keyH.upPressed == true && keyH.rightPressed == true){
            direction = "right";
            if (x + speed/Math.sqrt(2) + idle1.getWidth()/2 < gp.screenWidth) x += (int) (speed/Math.sqrt(2));
            if (y - speed/Math.sqrt(2) - idle1.getHeight()/2 > 0) y -= (int) (speed/Math.sqrt(2));
        }
        else if (keyH.downPressed == true && keyH.leftPressed == true){
            direction = "left";
            if (x - speed/Math.sqrt(2) - idle1.getWidth()/2 > 0) x -= (int) (speed/Math.sqrt(2));
            if (y + speed/Math.sqrt(2) + idle1.getHeight()/2 < gp.screenHeight) y += (int) (speed/Math.sqrt(2));
        }
        else if (keyH.downPressed == true && keyH.rightPressed == true){
            direction = "right";
            if (x + speed/Math.sqrt(2) + idle1.getWidth()/2 < gp.screenWidth) x += (int) (speed/Math.sqrt(2));
            if (y + speed/Math.sqrt(2) + idle1.getHeight()/2 < gp.screenHeight) y += (int) (speed/Math.sqrt(2));
        }
        else if (keyH.upPressed == true){
            direction = "idle";
            if (y - speed - idle1.getHeight()/2 > 0) y -= speed;
        }
        else if (keyH.downPressed == true){
            direction = "idle";
            if (y + speed + idle1.getHeight()/2 < gp.screenHeight) y += speed;
        }
        else if (keyH.leftPressed == true){
            direction = "left";
            if (x - speed - idle1.getWidth()/2 > 0) x -= speed;
        }
        else if (keyH.rightPressed == true){
            direction = "right";
            if (x + speed + idle1.getWidth()/2 < gp.screenWidth) x += speed;
        }
        else direction = "idle";

        //focus
        if (keyH.focusPressed == true){
            focusing = true;
            speed = 4;
        }
        else {
            focusing = false;
            speed = 8;
        }

        //shoot
        if (keyH.shootPressed == true && cooldown == 0){
            if (focusing == true){
                Shooting focus = new Shooting2(x, y, 90, 2);
                shoot.add(focus);
                Shooting amulet1 = new Shooting2(x, y, 90, 1);
                shoot.add(amulet1);
                Shooting amulet2 = new Shooting2(x, y, 92, 1);
                shoot.add(amulet2);
                Shooting amulet3 = new Shooting2(x, y, 88, 1);
                shoot.add(amulet3);
            }
            else {
                Shooting amulet1 = new Shooting2(x, y, 90, 1);
                shoot.add(amulet1);
                Shooting amulet2 = new Shooting2(x, y, 94, 1);
                shoot.add(amulet2);
                Shooting amulet3 = new Shooting2(x, y, 86, 1);
                shoot.add(amulet3);
                Shooting unfocus1 = new Shooting2(x - 30, y, 90, 3);
                shoot.add(unfocus1);
                Shooting unfocus2 = new Shooting2(x + 30, y, 90, 3);
                shoot.add(unfocus2);
            }
            cooldown = 5;
        }
        else if (keyH.shootPressed == false) {
            cooldown = 5;
        }
        cooldown--;

        //sprite
        spriteCounter++;
        if (spriteCounter > 10){
            spriteNum = (spriteNum + 1) % 5;
            spriteCounter = 0;
        }

        //hitbox
        playerHitbox.update(x, y);
        for (int i = 0; i < hitboxesPool.size(); i++){
            if (invicible > 0) break;
            if (checkCollide(playerHitbox, hitboxesPool.get(i))){
                miss++;
                invicible = 5*60;
                gp.playSE(4);
                break;
                //System.out.println("Got hit:" + miss + " at " + playerHitbox.x + " " + playerHitbox.y);
            }
        }
        if (invicible > 0) invicible--;

        timer++;
    }
}
