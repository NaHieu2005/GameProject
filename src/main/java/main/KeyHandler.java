package main;

import entity.Player;
import entity.Player2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed, shootPressed, focusPressed;
    GamePanel gp;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gp.gameState == gp.titleState){
            if (gp.ui.titleScreenState == 0){
                if (key == KeyEvent.VK_UP){
                    gp.ui.cursor--;
                    if (gp.ui.cursor < 0) gp.ui.cursor = 3;
                    gp.playSE(0);
                }

                if (key == KeyEvent.VK_DOWN){
                    gp.ui.cursor++;
                    if (gp.ui.cursor > 3) gp.ui.cursor = 0;
                        gp.playSE(0);
                }

                if (key == KeyEvent.VK_Z){
                    gp.playSE(2);
                    switch (gp.ui.cursor){
                        case 0:
                            gp.ui.titleScreenState = 1;
                            break;
                        case 1:
                            gp.ui.titleScreenState = 2;
                            break;
                        case 3:
                            System.exit(0);
                            break;
                    }
                }
            }

            else if (gp.ui.titleScreenState == 1){ //Char select
                if (key == KeyEvent.VK_ESCAPE){
                    gp.ui.characterState = 0;
                    gp.ui.titleScreenState = 0;
                    gp.playSE(1);
                }

                if (key == KeyEvent.VK_LEFT){
                    gp.ui.characterState++;
                    gp.ui.characterState %= 2;
                    gp.playSE(0);
                }

                if (key == KeyEvent.VK_RIGHT){
                    gp.ui.characterState++;
                    gp.ui.characterState %= 2;
                    gp.playSE(0);
                }

                if (key == KeyEvent.VK_Z){
                    gp.gameState = gp.playState;
                    gp.stopMusic();
                    gp.playMusic(6);
                    gp.playSE(2);
                    if (gp.ui.characterState == 0){
                        gp.player = new Player(gp,this);
                    }
                    if (gp.ui.characterState == 1){
                        gp.player = new Player2(gp,this);
                    }
                }
            }

            else if (gp.ui.titleScreenState == 2){ //Tutorial
                if (key == KeyEvent.VK_ESCAPE){
                    gp.ui.titleScreenState = 0;
                    gp.playSE(1);
                }
            }
        }

        else if (gp.gameState == gp.playState){
            if (key == KeyEvent.VK_ESCAPE){
                gp.gameState = gp.pauseState;
                gp.ui.cursor = 0;
                gp.playSE(8);
                gp.stopMusic();
            }
        }

        else if (gp.gameState == gp.pauseState){
            if (key == KeyEvent.VK_DOWN){
                gp.ui.cursor++;
                gp.playSE(0);
                if (gp.ui.cursor > 2) gp.ui.cursor = 0;
            }

            if (key == KeyEvent.VK_UP){
                gp.ui.cursor--;
                gp.playSE(0);
                if (gp.ui.cursor < 0) gp.ui.cursor = 2;
            }

            if (key == KeyEvent.VK_Z){
                gp.playSE(2);
                switch (gp.ui.cursor){
                    case 0: //return to game
                        gp.gameState = gp.playState;
                        gp.resumeMusic();
                        break;
                    case 1: //quit and return to title
                        gp.setup();
                        break;
                    case 2: //try again
                        gp.restart();
                        break;
                }
            }
        }

        if (key == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if (key == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_Z) {
            shootPressed = true;
        }
        if (key == KeyEvent.VK_SHIFT) {
            focusPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if (key == KeyEvent.VK_Z) {
            shootPressed = false;
        }
        if (key == KeyEvent.VK_SHIFT) {
            focusPressed = false;
        }
    }
}
