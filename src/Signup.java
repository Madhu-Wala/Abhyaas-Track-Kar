import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Signup extends JFrame {
    Signup(){
        Font f = new Font("Arial Rounded MT Bold", Font.BOLD, 40);
        Font f2 = new Font("Candara", Font.PLAIN, 22);
        JLabel l1=new JLabel("Abhyas TRACK-kar",JLabel.CENTER);
        JLabel l2=new JLabel("Signup",JLabel.CENTER);
        JLabel l3=new JLabel("Create username: ");
        JTextField t1 = new JTextField(10);
        JLabel l4=new JLabel("Enter email id: ");
        JTextField t2 = new JTextField(10);
        JLabel l5=new JLabel("Enter Stream: ");
        JTextField t3 = new JTextField(10);
        JLabel l6=new JLabel("Create Password: ");
        JTextField t4 = new JTextField(10);
        JLabel l7=new JLabel("Confirm Password: ");
        JTextField t5 = new JTextField(10);
        JButton b1=new JButton("Signup");
        JButton b2=new JButton("Back");

        l1.setFont(f);
        l2.setFont(f2);

        l3.setFont(f2);
        l4.setFont(f2);
        l5.setFont(f2);
        l6.setFont(f2);
        l7.setFont(f2);

        t1.setFont(f2);
        t2.setFont(f2);
        t3.setFont(f2);
        t4.setFont(f2);
        t5.setFont(f2);

        b1.setFont(f2);
        b2.setFont(f2);

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
                        0, height, new Color(198, 229, 255)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        gradientPanel.setLayout(null); // or whatever layout you want

        setContentPane(gradientPanel);  // Replaces getContentPane()

        Container c = getContentPane();
        c.setLayout(null);
        l1.setBounds(100, 50, 580, 50);
        l2.setBounds(250, 100, 300, 50);

        l3.setBounds(150, 200, 300, 50);
        l4.setBounds(150, 250, 300, 50);
        l5.setBounds(150, 300, 300, 50);
        l6.setBounds(150, 350, 300, 50);
        l7.setBounds(150, 400, 300, 50);

        t1.setBounds(350, 200, 300, 40);
        t2.setBounds(350, 250, 300, 40);
        t3.setBounds(350, 300, 300, 40);
        t4.setBounds(350, 350, 300, 40);
        t5.setBounds(350, 400, 300, 40);

        b1.setBounds(280, 550, 100, 40);
        b2.setBounds(440, 550, 100, 40);
        c.add(l1);
        c.add(l2);

        c.add(l3);
        c.add(l4);
        c.add(l5);
        c.add(l6);
        c.add(l7);

        c.add(t1);
        c.add(t2);
        c.add(t3);
        c.add(t4);
        c.add(t5);

        c.add(b1);
        c.add(b2);

        b1.addActionListener(a -> {
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            try (Connection con = DriverManager.getConnection(url, "C##MAJAKAAM", "majajava123")) {
                String sql = "INSERT INTO users_of_abhyas (uname, uemail, stream, password) VALUES (?, ?, ?, ?)";


                String uname = t1.getText().trim();
                String email = t2.getText().trim();
                String stream = t3.getText().trim();
                String pass = t4.getText().trim();
                String cpass = t5.getText().trim();

                if (uname.isEmpty() || email.isEmpty() || stream.isEmpty() || pass.isEmpty() || cpass.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required. Please fill them out.");
                    return;
                }

                if (!pass.equals(cpass)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match. Please re-enter.");
                    t4.setText("");
                    t5.setText("");
                    return;
                }

                String checkUserSql = "SELECT COUNT(*) FROM users_of_abhyas WHERE uname = ?";
                try (PreparedStatement checkStmt = con.prepareStatement(checkUserSql)) {
                    checkStmt.setString(1, t1.getText());
                    ResultSet rs = checkStmt.executeQuery();
                    rs.next();
                    int count = rs.getInt(1);
                    if (count > 0) {
                        JOptionPane.showMessageDialog(null, "Username already exists. Please choose a different one.");
                        return;
                    }
                }

                try (PreparedStatement pst = con.prepareStatement(sql)) {
                    pst.setString(1, uname);
                    pst.setString(2, email);
                    pst.setString(3, stream);
                    pst.setString(4, pass);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Signup Successful");
                    new Dashboard(uname);
                    dispose();
                }
            } catch (Exception e) {
                e.printStackTrace(); // good for debugging
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });

        b2.addActionListener(
                a->{
                    new First();
                    dispose();
                }
        );

        setVisible(true);
        setSize(800, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Signup");
    }
    public static void main(String[] args) {
        new Signup();
    }
}
