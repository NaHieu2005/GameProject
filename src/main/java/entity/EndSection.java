package entity;

import main.GamePanel;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class EndSection {
    GamePanel gp;
    Font font;

    public EndSection(GamePanel gp){
        this.gp = gp;
        gp.yuyuko = null;
        InputStream is = getClass().getResourceAsStream("/font/DCAi-W5-WIN-RKSJ-H-01.ttf");
        try {
            font = Font.createFont(font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(){

    }

    public void draw(Graphics2D g2d){
        g2d.setFont(font);
    }
}
