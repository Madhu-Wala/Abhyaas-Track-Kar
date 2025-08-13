import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class DeleteSub extends JFrame {
    DeleteSub(String username){
        Font f = new Font("Arial Rounded MT Bold", Font.BOLD, 40);
        Font f2 = new Font("Candara", Font.PLAIN, 22);

        JLabel l1 = new JLabel("Abhyas TRACK-kar", JLabel.CENTER);
        JLabel l2 = new JLabel("Delete Subject", JLabel.CENTER);
        JLabel l3 = new JLabel(username);
        JLabel l4=new JLabel("Enter Subject to delete:");
        JTextField t1=new JTextField(10);
        JButton delete=new JButton("Delete");
        JButton back=new JButton("Back");

        l1.setFont(f);
        l2.setFont(f2);
        l3.setFont(f2);
        l4.setFont(f2);
        t1.setFont(f2);
        delete.setFont(f2);
        back.setFont(f2);

        delete.setBackground(new Color(239, 1, 1));
        delete.setForeground(Color.white);
        back.setBackground(new Color(255, 183, 0));

        l1.setBounds(100, 50, 580, 50);
        l2.setBounds(250, 100, 300, 50);
        l3.setBounds(100, 150, 300, 50);
        l4.setBounds(150, 250, 300, 50);
        t1.setBounds(350, 250, 300, 40);
        delete.setBounds(250, 350, 100, 40);
        back.setBounds(400, 350, 100, 40);

        l1.setBounds(100, 50, 580, 50);
        l2.setBounds(250, 100, 300, 50);
        l3.setBounds(100, 150, 300, 50);
        l4.setBounds(100, 250, 400, 50);
        t1.setBounds(350, 250, 300, 40);
        delete.setBounds(250, 350, 100, 40);
        back.setBounds(400, 350, 100, 40);

        Container c=getContentPane();
        c.setLayout(null);

        ImageIcon userIcon = new ImageIcon("C:\\Users\\Madhura Walawalkar\\Downloads\\1-personal-center-20x20-48.png");
        Image scaledImage = userIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JLabel userLogo = new JLabel(new ImageIcon(scaledImage));
        userLogo.setBounds(50, 150, 48, 48); // Adjust as needed
        c.add(userLogo);
        c.add(l1);
        c.add(l2);
        c.add(l3);
        c.add(l4);
        c.add(t1);
        c.add(delete);
        c.add(back);

        delete.addActionListener(
                a->{
                    String url="jdbc:oracle:thin:@localhost:1521:orcl";
                    int userId=0;
                    try(Connection con= DriverManager.getConnection(url,"C##MAJAKAAM","majajava123")){
                        String sql="select user_id from users_of_abhyas where uname= ?";
                        try (PreparedStatement pst=con.prepareStatement(sql)){
                            pst.setString(1, username);
                            ResultSet rs= pst.executeQuery();
                            if(rs.next()){
                                userId=rs.getInt("user_id");
                            }
                        }

                        String deleteSubSQL = "DELETE FROM subjects WHERE subname = ? AND user_id = ?";
                        try (PreparedStatement pst = con.prepareStatement(deleteSubSQL)) {
                            pst.setString(1, t1.getText()); // subject name from input
                            pst.setInt(2, userId);          // user ID from earlier fetch
                            int rowsAffected = pst.executeUpdate();
                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(null, "Subject deleted successfully");
                            } else {
                                JOptionPane.showMessageDialog(null, "Subject not found");
                            }
                            t1.setText("");
                        }


                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                }
        );

        back.addActionListener(
                a->{
                    new Dashboard(username);
                    dispose();
                }
        );

        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Delete Subject");
    }
    public static void main(String[] args) {
        new DeleteSub("Madhura");
    }
}
