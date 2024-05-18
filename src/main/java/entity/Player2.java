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
            idle1 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_idle1.png"));
            idle2 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_idle2.png"));
            idle3 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_idle3.png"));
            idle4 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_idle4.png"));
            idle5 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_idle5.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_left2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_left3.png"));
            left4 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_left4.png"));
            left5 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_left5.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_right2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_right3.png"));
            right4 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_right4.png"));
            right5 = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_right5.png"));
            yinyang = ImageIO.read(getClass().getResourceAsStream("/sprite/magic.png"));
            hitbox = ImageIO.read(getClass().getResourceAsStream("/sprite/player_hitbox.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    void shoot(){
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
            cooldown = 6;
        }
        else if (keyH.shootPressed == false) {
            cooldown = 3;
        }
        cooldown--;
    }

    @Override
    void focus(){
        if (keyH.focusPressed == true){
            focusing = true;
            speed = 4;
        }
        else {
            focusing = false;
            speed = 8;
        }
    }
}
