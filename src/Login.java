import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Login extends JFrame {

    private boolean isVisible = false;  // Class-level toggle flag
    Login(){
        Font f = new Font("Arial Rounded MT Bold", Font.BOLD, 40);
        Font f2 = new Font("Candara", Font.PLAIN, 22);

        JLabel l1=new JLabel("Abhyas TRACK-kar",JLabel.CENTER);
        JLabel l2=new JLabel("Login",JLabel.CENTER);
        JLabel l3=new JLabel("Enter username: ");
        JTextField t1 = new JTextField(10);
        JLabel l4=new JLabel("Enter Password: ");
//        JTextField t2 = new JTextField(10);
        JPasswordField t2=new JPasswordField(10);
        t2.setEchoChar('•');

        JButton toggleButton = new JButton("👁");

        JButton b1=new JButton("Login");
        JButton b2=new JButton("Back");

        l1.setFont(f);
        l2.setFont(f2);
        l3.setFont(f2);
        l4.setFont(f2);
        t1.setFont(f2);
        t2.setFont(f2);
        b1.setFont(f2);
        b2.setFont(f2);
        toggleButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        b1.setBackground(new Color(34, 130, 0));
        b2.setBackground(new Color(113, 6, 184));
        b1.setForeground(Color.white);
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
                        0, height, new Color(255, 248, 215)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        gradientPanel.setLayout(null); // or whatever layout you want

        setContentPane(gradientPanel);  // Replaces getContentPane()
        Container c = getContentPane();  // Now c is your gradient panel

        c.setLayout(null);

        l1.setBounds(100, 50, 580, 50);
        l2.setBounds(250, 100, 300, 50);
        l3.setBounds(150, 200, 300, 50);
        l4.setBounds(150, 250, 300, 50);
        t1.setBounds(350, 200, 300, 40);
        t2.setBounds(350, 250, 300, 40);
        toggleButton.setBounds(680, 250, 80, 40);
        b1.setBounds(250, 350, 100, 40);
        b2.setBounds(400, 350, 100, 40);

        c.add(l1);
        c.add(l2);
        c.add(l3);
        c.add(l4);
        c.add(t1);
        c.add(t2);
        c.add(b1);
        c.add(b2);
        c.add(toggleButton);
        toggleButton.addActionListener(e -> {
            if (isVisible) {
                t2.setEchoChar('•');     // Mask
                toggleButton.setText("👁");          // Optional: change button icon/text
            } else {
                t2.setEchoChar((char)0); // Unmask
                toggleButton.setText("🙈");
            }
            isVisible = !isVisible; // Flip the flag
        });

        b1.addActionListener(
                a->{
                    String url="jdbc:oracle:thin:@localhost:1521:orcl";
                    try(Connection con= DriverManager.getConnection(url,"C##MAJAKAAM","majajava123")){
                        String sql="select * from users_of_abhyas where uname=? and password=?";
                        try(PreparedStatement pst=con.prepareStatement(sql)){
                            pst.setString(1, t1.getText());
                            pst.setString(2, t2.getText());

                            ResultSet rs=pst.executeQuery();

                            if(rs.next()){
                                JOptionPane.showMessageDialog(null, "Login successful");
                                new Dashboard(t1.getText());
                                dispose();
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "User does not exist");
                            }
                        }

                    }catch (Exception e){
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                }
        );

        b2.addActionListener(
                a->{
                    new First();
                    dispose();
                }
        );

        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Login");
    }
    public static void main(String[] args) {
        new Login();
    }
}
