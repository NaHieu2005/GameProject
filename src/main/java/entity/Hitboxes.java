package entity;

public class Hitboxes {
    double x, y, r;
    public Hitboxes(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public static boolean checkCollide(Hitboxes a, Hitboxes b) {
        double dist = Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
        //System.out.println(b.x + " " + b.y + " " + b.r + " " + dist);
        if (dist < a.r + b.r) {
            return true;
        }
        return false;
    }

    public void update(double x, double y){
        this.x = x;
        this.y = y;
    }
}
