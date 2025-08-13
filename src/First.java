import javax.swing.*;
import java.awt.*;

class First extends JFrame {
    First(){
        Font f = new Font("Arial Rounded MT Bold", Font.BOLD, 40);
        Font f2 = new Font("Candara", Font.PLAIN, 22);

        JLabel l1=new JLabel("Abhyas TRACK-kar",JLabel.CENTER);

        JButton b1=new JButton("Login");
        JButton b2=new JButton("Signup");

        l1.setFont(f);
        b1.setFont(f2);
        b2.setFont(f2);
        b1.setBackground(new Color(255, 200, 0));
        Color c1=new Color(1, 89, 159);
        b2.setBackground(c1);
        b2.setForeground(Color.white);

        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                // Vertical gradient from top to bottom
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255, 255, 255),
                        0, height, new Color(187, 243, 244)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        gradientPanel.setLayout(null); // or whatever layout you want

        setContentPane(gradientPanel);  // Replaces getContentPane()

        Container c=getContentPane();
        c.setLayout(null);

        l1.setBounds(200, 50, 580, 60);
        b1.setBounds(250, 250, 150, 60);
        b2.setBounds(600, 250, 150, 60);

        c.add(l1);
        c.add(b1);
        c.add(b2);

        b1.addActionListener(
                a->{
                    new Login();
                    dispose();
                }
        );
        b2.addActionListener(
                a->{
                    new Signup();
                    dispose();
                }
        );

        setVisible(true);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Abhyas TRACK-kar");
    }
    public static void main(String[] args) {
        new First();
    }
}
