package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gp;
    Font pcb_font;
    Graphics2D g2;
    public int cursor = 0;
    public int titleScreenState = 0;
    public int characterState = 0;

    public UI(GamePanel gp) {
        this.gp = gp;
        InputStream is = getClass().getResourceAsStream("/font/DCAi-W5-WIN-RKSJ-H-01.ttf");
        try {
            pcb_font = Font.createFont(pcb_font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;

        if (gp.gameState == gp.titleState){
            drawTitleScreen();
        }

        if (gp.gameState == gp.pauseState){
            drawPauseScreen();
        }
    }

    private void drawPauseScreen() {
        BufferedImage pause = null, return_to_game = null, quit_to_title = null, try_again = null, blur = null;
        int x = gp.screenWidth/2;
        int y = gp.screenHeight/2;

        try {
            pause = ImageIO.read(getClass().getResourceAsStream("/player/pause.png"));
            return_to_game = ImageIO.read(getClass().getResourceAsStream("/player/return_to_game.png"));
            quit_to_title = ImageIO.read(getClass().getResourceAsStream("/player/quit_to_title.png"));
            try_again = ImageIO.read(getClass().getResourceAsStream("/player/try_again.png"));
            blur = ImageIO.read(getClass().getResourceAsStream("/player/blurpane.png"));
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
        //System.out.println(titleState);
        switch (titleScreenState){
            case 0:
                //TITLE
                try {
                    title = ImageIO.read(getClass().getResourceAsStream("/player/title.png"));
                    bg = ImageIO.read(getClass().getResourceAsStream("/player/title_bg.png"));
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
                        s_bg = ImageIO.read(getClass().getResourceAsStream("/player/select_bg.png"));
                        charImg = ImageIO.read(getClass().getResourceAsStream("/player/reimu_select.png"));
                        choose_girl = ImageIO.read(getClass().getResourceAsStream("/player/choose_girl.png"));
                        name = ImageIO.read(getClass().getResourceAsStream("/player/reimu_name.png"));
                        stat = ImageIO.read(getClass().getResourceAsStream("/player/reimu_stat.png"));
                        diff = ImageIO.read(getClass().getResourceAsStream("/player/lunatic.png"));
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
                        s_bg = ImageIO.read(getClass().getResourceAsStream("/player/select_bg.png"));
                        charImg = ImageIO.read(getClass().getResourceAsStream("/player/marisa_select.png"));
                        choose_girl = ImageIO.read(getClass().getResourceAsStream("/player/choose_girl.png"));
                        name = ImageIO.read(getClass().getResourceAsStream("/player/marisa_name.png"));
                        stat = ImageIO.read(getClass().getResourceAsStream("/player/marisa_stat.png"));
                        diff = ImageIO.read(getClass().getResourceAsStream("/player/lunatic.png"));
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
                    bg2 = ImageIO.read(getClass().getResourceAsStream("/player/manual_bg.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2.drawImage(bg2, 0, 0, gp.screenWidth, gp.screenHeight, null);

                g2.setColor(Color.PINK);
                g2.drawString("Introduction", 25, 100);

                g2.setFont(new Font("Arial", Font.PLAIN, 25));
                g2.setColor(Color.WHITE);
                g2.drawString("- This is game about shooting down enemies while", 30, 150);
                g2.drawString("dodging enemy bullets.", 30, 200);
                g2.drawString("- Strike precisely with your memory and skills, aiming", 30, 250);
                g2.drawString("for a focused, short-term, and decisive battle!", 30, 300);

                g2.setFont(pcb_font);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 45f));
                g2.setColor(Color.PINK);
                g2.drawString("Control", 25, 400);

                g2.setFont(new Font("Arial", Font.BOLD, 25));
                g2.setColor(Color.WHITE);

                g2.drawString("Arrow:", 30, 450);
                g2.drawString("Z:", 30, 500);
                g2.drawString("X:", 30, 550);
                g2.drawString("Shift:", 30, 600);
                g2.drawString("Esc:", 30, 650);

                g2.setFont(new Font("Arial", Font.BOLD, 25));
                g2.setColor(Color.WHITE);
                g2.drawString("Player movement", 150, 450);
                g2.drawString("Shoot", 150, 500);
                g2.drawString("SpellCard", 150, 550);
                g2.drawString("Slower movement (Focus)", 150, 600);
                g2.drawString("Pause", 150, 650);
                break;
            case 3:


        }
    }

    public int getXCenteredText(String text){
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
}
