package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gp;
    Font pcb_font, handwritter_font;
    Graphics2D g2;
    public int cursor = 0;
    public int titleScreenState = 0;
    public int characterState = 0;
    public int musicState = 0;
    int timer = 0;
    int damage_score = 0, spellBonus_score = 0, liveBonus_score = 0;
    double spriteNum = 0;
    double creditNum = 800;

    public UI(GamePanel gp) {
        this.gp = gp;
        InputStream is = getClass().getResourceAsStream("/font/DCAi-W5-WIN-RKSJ-H-01.ttf");
        try {
            pcb_font = Font.createFont(pcb_font.TRUETYPE_FONT, is);
            handwritter_font = Font.createFont(handwritter_font.TRUETYPE_FONT, getClass().getResourceAsStream("/font/SVN-Fords-Folly.ttf"));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;
        timer++;

        if (gp.gameState == gp.titleState){
            drawTitleScreen();
        }

        if (gp.gameState == gp.playState){
            drawPlayScreen();
        }

        if (gp.gameState == gp.endState){
            drawEndScreen();
        }

        if (gp.gameState == gp.pauseState){
            drawPauseScreen();
        }

        if (gp.gameState == gp.gameOverState){
            drawGameOverScreen();
        }
    }

    private void drawGameOverScreen(){
        BufferedImage blur = null;
        String text = "Game Over";
        try {
            blur = ImageIO.read(getClass().getResourceAsStream("/sprite/blurpane.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2.drawImage(blur, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.setFont(handwritter_font);
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));
        g2.drawString(text, 260, 400);
    }

    private void drawPlayScreen() {
        g2.setColor(Color.RED);
        BufferedImage live0 = null, live1 = null, yuyuko_name = null;

        try {
            live0 = ImageIO.read(getClass().getResourceAsStream("/sprite/live0.png"));
            live1 = ImageIO.read(getClass().getResourceAsStream("/sprite/live1.png"));
            yuyuko_name = ImageIO.read(getClass().getResourceAsStream("/sprite/yuyuko_name.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2.drawImage(yuyuko_name, 10, 10, null);

        //live draw
        for (int i = -2; i <= 2; i++){
            g2.drawImage(live0, gp.screenWidth/2 - live0.getWidth()/2 + 30*i, 20, null);
        }

        for (int i = 0; i < gp.player.live; i++){
            g2.drawImage(live1, gp.screenWidth/2 - live0.getWidth()/2 + 30*(i - 2), 20, null);
        }

        //score draw
        if (gp.section == gp.endSection){
            if (damage_score < gp.damage_score) {
                damage_score = Math.min(damage_score + 1000000, gp.damage_score);
            }
            else if (liveBonus_score < gp.player.live * 2000000) {
                liveBonus_score = Math.min(liveBonus_score + 100000, gp.player.live * 2000000);
            }
            else if (spellBonus_score < gp.spellBonus_score) {
                spellBonus_score = Math.min(spellBonus_score + 100000, gp.spellBonus_score);
            }
            else gp.isEnd = true;

            g2.setFont(pcb_font);
            drawString("All Clear!", 250, 200, Color.YELLOW, 30);
            drawString("Damage Score: " + damage_score, 100, 300, Color.YELLOW, 30);
            drawString("Live Bonus: " + liveBonus_score, 100, 350, Color.YELLOW, 30);
            drawString("Spellcard Bonus: " + spellBonus_score, 100, 400, Color.YELLOW, 30);
            drawString("Total Score: " + (damage_score + liveBonus_score + spellBonus_score), 100, 475, Color.YELLOW, 30);
        }
    }

    private void drawEndScreen() {
        BufferedImage bg = null;
        try {
            bg = ImageIO.read(getClass().getResourceAsStream("/sprite/ending_bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2.setFont(handwritter_font);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30f));
        g2.setColor(Color.BLACK);
        g2.drawImage(bg, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.drawString("7th Project Shrine Maiden", 24, (int)(creditNum));
        g2.drawString("Perfect Cherry Blossom", 28, (int)(creditNum+75));
        g2.drawString("Original, Graphic and Music", 23 , (int)(creditNum+575));
        g2.drawString("ZUN", 130, (int)(creditNum+650));
        g2.drawString("Programming, Reference", 26, (int)(creditNum+1150));
        g2.drawString("nahieu2005", 99, (int)(creditNum+1225));
        g2.drawString("19/5/2024", 100 , (int)(creditNum+1300));
        g2.drawString("Made for OOP game project", 20 , (int)(creditNum+1375));
        g2.drawString("Thanks for playing!", 37 , (int)(creditNum+1875));
        if (creditNum + 1875 > gp.screenHeight/2) creditNum--;
        else{
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40f));
            g2.drawString("_Next Phantasm", 400, 730);
        }
    }

    private void drawPauseScreen() {
        BufferedImage pause = null, return_to_game = null, quit_to_title = null, try_again = null, blur = null;
        int x = gp.screenWidth/2;
        int y = gp.screenHeight/2;

        try {
            pause = ImageIO.read(getClass().getResourceAsStream("/sprite/pause.png"));
            return_to_game = ImageIO.read(getClass().getResourceAsStream("/sprite/return_to_game.png"));
            quit_to_title = ImageIO.read(getClass().getResourceAsStream("/sprite/quit_to_title.png"));
            try_again = ImageIO.read(getClass().getResourceAsStream("/sprite/try_again.png"));
            blur = ImageIO.read(getClass().getResourceAsStream("/sprite/blurpane.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        g2.drawImage(return_to_game, x - return_to_game.getWidth()/2, y,null);
        g2.drawImage(quit_to_title, x - quit_to_title.getWidth()/2, y + 55, null);
        g2.drawImage(try_again, x - try_again.getWidth()/2, y + 110, null);

        g2.drawImage(blur, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.drawImage(pause, x - pause.getWidth()/2, y - 125, null);
        switch (cursor){
            case 0: //return to game
                g2.drawImage(return_to_game, x - return_to_game.getWidth()/2, y,null);
                break;
            case 1: //quit to title
                g2.drawImage(quit_to_title, x - quit_to_title.getWidth()/2, y + 55, null);
                break;
            case 2:
                g2.drawImage(try_again, x - try_again.getWidth()/2, y + 110, null);
                break;
        }
    }

    public void drawTitleScreen(){
        g2.setFont(pcb_font);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 45f));
        String text = "東方妖々夢";
        int x = gp.screenWidth/2;
        int y = 150;
        BufferedImage title = null;
        BufferedImage bg = null;
        BufferedImage bg2 = null;

        switch (titleScreenState){
            case 0:
                //TITLE
                try {
                    title = ImageIO.read(getClass().getResourceAsStream("/sprite/title.png"));
                    bg = ImageIO.read(getClass().getResourceAsStream("/sprite/title_bg.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2.drawImage(bg, 0, 0, gp.screenWidth, gp.screenHeight, null);
                g2.drawImage(title, x - title.getWidth()/2, y - title.getHeight()/2, title.getWidth(), title.getHeight(), null);

                //MENU
                text = "Start";
                x = 350;
                y = 400;
                g2.setColor(Color.BLACK);
                g2.drawString(text, x + 5, y + 5);
                g2.setColor(Color.GRAY);
                if (cursor == 0) g2.setColor(Color.WHITE);
                g2.drawString(text, x, y);

                text = "Manual";
                x = 420;
                y = 470;
                g2.setColor(Color.BLACK);
                g2.drawString(text, x + 5, y + 5);
                g2.setColor(Color.GRAY);
                if (cursor == 1) g2.setColor(Color.WHITE);
                g2.drawString(text, x, y);

                text = "Music Room";
                x = 380;
                y = 550;
                g2.setColor(Color.BLACK);
                g2.drawString(text, x + 5, y + 5);
                g2.setColor(Color.GRAY);
                if (cursor == 2) g2.setColor(Color.WHITE);
                g2.drawString(text, x, y);

                text = "Quit";
                x = 450;
                y = 625;
                g2.setColor(Color.BLACK);
                g2.drawString(text, x + 5, y + 5);
                g2.setColor(Color.GRAY);
                if (cursor == 3) g2.setColor(Color.WHITE);
                g2.drawString(text, x, y);

                text = "Press Z and Arrows to navigate";
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                g2.setColor(Color.WHITE);
                if (timer % 60 < 30) g2.drawString(text, 175, 750);
                break;
            case 1: //Character Select
                BufferedImage charImg = null;
                BufferedImage choose_girl = null;
                BufferedImage s_bg = null;
                BufferedImage name = null;
                BufferedImage stat = null;
                BufferedImage diff = null;

                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40f));
                g2.setColor(Color.PINK);
                x = 400;
                y = 500;

                if (characterState == 0){ //Reimu
                    //IMAGE
                    try {
                        s_bg = ImageIO.read(getClass().getResourceAsStream("/sprite/select_bg.png"));
                        charImg = ImageIO.read(getClass().getResourceAsStream("/sprite/reimu_select.png"));
                        choose_girl = ImageIO.read(getClass().getResourceAsStream("/sprite/choose_girl.png"));
                        name = ImageIO.read(getClass().getResourceAsStream("/sprite/reimu_name.png"));
                        stat = ImageIO.read(getClass().getResourceAsStream("/sprite/reimu_stat.png"));
                        diff = ImageIO.read(getClass().getResourceAsStream("/sprite/lunatic.png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    g2.drawImage(s_bg, 0, 0, gp.screenWidth, gp.screenHeight, null);
                    g2.drawImage(choose_girl, gp.screenWidth/2 - choose_girl.getWidth()/2, 100 - choose_girl.getHeight()/2, null);
                    g2.drawImage(charImg, x - charImg.getWidth()/2, y - charImg.getHeight()/2, null);
                    g2.drawImage(name, x + 20 - name.getWidth()/2, y + 50 - name.getHeight()/2, null);
                    g2.drawImage(stat, x + 20 - stat.getWidth()/2, y + 130 - stat.getHeight()/2, null);
                    g2.drawImage(diff, 25, 675, null);
                    //g2.drawImage(charPreview, 425, 400, 100, 100, null);
                }

                if (characterState == 1){//Marisa
                    //IMAGE
                    try {
                        s_bg = ImageIO.read(getClass().getResourceAsStream("/sprite/select_bg.png"));
                        charImg = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_select.png"));
                        choose_girl = ImageIO.read(getClass().getResourceAsStream("/sprite/choose_girl.png"));
                        name = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_name.png"));
                        stat = ImageIO.read(getClass().getResourceAsStream("/sprite/marisa_stat.png"));
                        diff = ImageIO.read(getClass().getResourceAsStream("/sprite/lunatic.png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    g2.drawImage(s_bg, 0, 0, gp.screenWidth, gp.screenHeight, null);
                    g2.drawImage(choose_girl, gp.screenWidth/2 - choose_girl.getWidth()/2, 100 - choose_girl.getHeight()/2, null);
                    g2.drawImage(charImg, x - charImg.getWidth()/2, y - charImg.getHeight()/2, null);
                    g2.drawImage(name, x + 20 - name.getWidth()/2, y + 50 - name.getHeight()/2, null);
                    g2.drawImage(stat, x + 20 - stat.getWidth()/2, y + 130 - stat.getHeight()/2, null);
                    g2.drawImage(diff, 25, 675, null);
                    //g2.drawImage(charPreview, 425, 400, 100, 100, null);
                }

                break;
            case 2: //Manual
                try {
                    bg2 = ImageIO.read(getClass().getResourceAsStream("/sprite/manual_bg.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2.drawImage(bg2, 0, 0, gp.screenWidth, gp.screenHeight, null);

                g2.setColor(Color.PINK);
                drawString("Introduction", 25, 100, Color.PINK, 45f);

                g2.setFont(new Font("Arial", Font.PLAIN, 25));
                g2.setColor(Color.WHITE);
                g2.drawString("- This is game about shooting down enemies while", 30, 150);
                g2.drawString("dodging enemy bullets.", 30, 200);
                g2.drawString("- Strike precisely with your memory and skills, aiming", 30, 250);
                g2.drawString("for a focused, short-term, and decisive battle!", 30, 300);

                g2.setFont(pcb_font);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 45f));
                g2.setColor(Color.PINK);
                drawString("Control", 25, 400, Color.PINK, 45f);

                g2.setFont(new Font("Arial", Font.BOLD, 25));
                g2.setColor(Color.WHITE);

                g2.drawString("Arrow:", 30, 450);
                g2.drawString("Z:", 30, 500);
                g2.drawString("Shift:", 30, 550);
                g2.drawString("Esc:", 30, 600);

                g2.setFont(new Font("Arial", Font.BOLD, 25));
                g2.setColor(Color.WHITE);
                g2.drawString("Player movement", 150, 450);
                g2.drawString("Shoot", 150, 500);
                g2.drawString("Slower movement (Focus)", 150, 550);
                g2.drawString("Pause", 150, 600);
                break;
            case 3:
                BufferedImage gif = null;
                String filename = "/gif/reimufumo_" + (int)spriteNum + ".png";

                try {
                    bg2 = ImageIO.read(getClass().getResourceAsStream("/sprite/select_bg.png"));
                    gif = ImageIO.read(getClass().getResourceAsStream(filename));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                g2.drawImage(bg2, 0, 0, gp.screenWidth, gp.screenHeight, null);
                g2.drawImage(gif, gp.screenWidth/2 - gif.getWidth()/2, 425, null);
//                g2.setFont(pcb_font);
//                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20f));
//                g2.setColor(Color.GRAY);

                drawString("Music room", 25, 100, Color.WHITE, 50f);
                drawString("01. Ghostly Dream ~ Snow or Cherry Petal", 25, 200, Color.GRAY, 20f);
                drawString("02. Bloom Nobly, Ink-Black Cherry Blossom ~ Border of Life", 25, 250, Color.GRAY, 20f);
                drawString("03. Border of Life", 25, 300, Color.GRAY, 20f);
                drawString("04. Sakura, Sakura ~ Japanize Dream..", 25, 350, Color.GRAY, 20f);
                switch (musicState){
                    case 0:
                        drawString("01. Ghostly Dream ~ Snow or Cherry Petal", 25, 200, Color.WHITE, 20f);
                        break;
                    case 1:
                        drawString("02. Bloom Nobly, Ink-Black Cherry Blossom ~ Border of Life", 25, 250, Color.WHITE, 20f);
                        break;
                    case 2:
                        drawString("03. Border of Life", 25, 300, Color.WHITE, 20f);
                        break;
                    case 3:
                        drawString("04. Sakura, Sakura ~ Japanize Dream..", 25, 350, Color.WHITE, 20f);
                        break;
                }

                spriteNum = (spriteNum + 0.2)%20;
                break;
        }
    }

    private void drawString(String str, int x, int y, Color c, float size) {
        g2.setFont(pcb_font);

        g2.setColor(Color.BLACK);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, size));
        g2.drawString(str, x + 3, y + 3);
        g2.drawString(str, x + 4, y + 4);
        g2.drawString(str, x + 5, y + 5);

        g2.setColor(c);
        g2.drawString(str, x, y);
    }

}
