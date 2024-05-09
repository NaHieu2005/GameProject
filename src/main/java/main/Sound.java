package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/se_select00.wav");
        soundURL[1] = getClass().getResource("/sound/se_cancel00.wav");
        soundURL[2] = getClass().getResource("/sound/se_ok00.wav");
        soundURL[3] = getClass().getResource("/sound/se_damage00.wav");
        soundURL[4] = getClass().getResource("/sound/se_pldead00.wav");
        soundURL[5] = getClass().getResource("/sound/01. Ghostly Dream ~ Snow or Cherry Petal.wav");
        soundURL[6] = getClass().getResource("/sound/13. Bloom Nobly, Ink-Black Cherry Blossom ~ Border of Life.wav");
        soundURL[7] = getClass().getResource("/sound/14. Border of Life.wav");
    }

    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }
}
