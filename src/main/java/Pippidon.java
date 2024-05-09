import javax.swing.*;
import java.awt.*;

public class Pippidon extends JFrame {
    public Pippidon(){
        initUI();
    }

    private void initUI(){
        Icon ic = new ImageIcon("C:\\Users\\ADMIN\\OneDrive\\Documents\\Pts\\Pipidon\\Yuyuko Don\\yuyukodonidle.gif");
        JLabel label = new JLabel(ic);
        add(label);

        pack();
        setTitle("Pippidon Animation");
        getContentPane().setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) {
        Pippidon don = new Pippidon();
        don.setVisible(true);
    }
}
