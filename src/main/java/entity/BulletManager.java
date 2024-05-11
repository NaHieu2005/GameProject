package entity;

import java.util.ArrayList;

public class BulletManager implements Runnable{
    ArrayList<BulletSpawner> purple_butterfly = new ArrayList<>();

    ArrayList<BulletSpawner> blue_butterfly = new ArrayList<>();

    ArrayList<BulletSpawner> red_butterfly_1 = new ArrayList<>();
    ArrayList<BulletSpawner> red_butterfly_2 = new ArrayList<>();
    ArrayList<BulletSpawner> red_butterfly_3 = new ArrayList<>();
    ArrayList<BulletSpawner> red_butterfly_4 = new ArrayList<>();

    ArrayList<BulletSpawner> bubble = new ArrayList<>();
    public int wave = 0;
    double x, y;
    Thread thread;

    public BulletManager(double x, double y, int wave) {
        this.x = x;
        this.y = y;
        this.wave = wave;
        start();
    }

    public void blue_butterfly(double x, double y, double speed){
        double angle = 90/((double)wave + 6);
        BulletSpawner b;

        for (double i = 0; i < 360; i+=angle) {
            b = new BulletSpawner(x, y, speed, i);
            b.setImage("/player/blue_butterfly.png");
            b.setHitbox(6);
            blue_butterfly.add(b);
        }
    }

    public void purple_butterfly(double x, double y, double speed){
        double angle = 90/((double)wave + 6);
        BulletSpawner b;

        for (double i = angle/2; i < 360; i+=angle) {
            b = new BulletSpawner(x, y, speed, i);
            b.setImage("/player/purple_butterfly.png");
            b.setHitbox(6);
            purple_butterfly.add(b);
        }
    }

    public void red_butterfly(double x, double y, double speed){
        double angle = 11.25;
        double angle_change = 0.4;
        BulletSpawner b;

        for (double i = 0; i < 360; i+=angle) {
            b = new BulletSpawner(x, y, speed, i);
            b.setImage("/player/red_butterfly.png");
            b.setHitbox(6);
            b.setAngle_change(angle_change);
            red_butterfly_1.add(b);
        }

        for (double i = angle/4; i < 360; i+=angle) {
            b = new BulletSpawner(x, y, speed, i);
            b.setImage("/player/red_butterfly.png");
            b.setHitbox(6);
            b.setAngle_change(angle_change);
            red_butterfly_2.add(b);
        }

        for (double i = 0; i < 360; i+=angle) {
            b = new BulletSpawner(x, y, speed, i);
            b.setImage("/player/red_butterfly.png");
            b.setHitbox(6);
            b.setAngle_change(-angle_change);
            red_butterfly_3.add(b);
        }

        for (double i = 3*angle/4; i < 360; i+=angle) {
            b = new BulletSpawner(x, y, speed, i);
            b.setImage("/player/red_butterfly.png");
            b.setHitbox(6);
            b.setAngle_change(-angle_change);
            red_butterfly_4.add(b);
        }
    }

    public void bubble(double x, double y, double speed){
        double angle = 15;
        BulletSpawner b;
        for (double i = angle/2; i < 360; i+=angle) {
            b = new BulletSpawner(x, y, speed, i);
            b.setImage("/player/red_bubble.png");
            b.setHitbox(24);
            bubble.add(b);
        }
    }

    public void start(){
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        blue_butterfly(x, y, 3.25);
        purple_butterfly(x, y, 3.25);
        red_butterfly(x, y, 3);
        bubble(x, y, 3);
        System.out.println(wave);
    }
}
